// Global variables
let selectedFile = null;
let convertedPdfBlob = null;

// DOM Elements
const wordFileInput = document.getElementById('wordFile');
const fileLabel = document.querySelector('.file-label');
const fileInfo = document.getElementById('fileInfo');
const advancedToggle = document.getElementById('advancedToggle');
const optionsContainer = document.getElementById('optionsContainer');
const convertBtn = document.getElementById('convertBtn');
const resetBtn = document.getElementById('resetBtn');
const downloadBtn = document.getElementById('downloadBtn');
const statusSection = document.getElementById('statusSection');
const statusMessage = document.getElementById('statusMessage');
const progressFill = document.getElementById('progressFill');
const downloadSection = document.getElementById('downloadSection');

// Option inputs
const marginTopInput = document.getElementById('marginTop');
const marginBottomInput = document.getElementById('marginBottom');
const marginLeftInput = document.getElementById('marginLeft');
const marginRightInput = document.getElementById('marginRight');
const pageOrientationSelect = document.getElementById('pageOrientation');
const pageSizeSelect = document.getElementById('pageSize');
const compressImagesCheckbox = document.getElementById('compressImages');

// Event Listeners
fileLabel.addEventListener('click', () => wordFileInput.click());

wordFileInput.addEventListener('change', handleFileSelect);

advancedToggle.addEventListener('click', toggleAdvancedOptions);

convertBtn.addEventListener('click', handleConversion);

resetBtn.addEventListener('click', resetForm);

downloadBtn.addEventListener('click', downloadPdf);

// File selection handler
function handleFileSelect(event) {
    selectedFile = event.target.files[0];

    if (!selectedFile) {
        return;
    }

    // Validate file type
    const validTypes = [
        'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
        'application/msword'
    ];

    if (!validTypes.includes(selectedFile.type)) {
        showFileError('Please select a valid Word document (.docx or .doc)');
        selectedFile = null;
        return;
    }

    // Validate file size (max 10MB)
    const maxSize = 10 * 1024 * 1024;
    if (selectedFile.size > maxSize) {
        showFileError('File size exceeds 10MB limit');
        selectedFile = null;
        return;
    }

    showFileSuccess(`✓ File selected: ${selectedFile.name}`);
    convertBtn.disabled = false;
}

function showFileSuccess(message) {
    fileInfo.textContent = message;
    fileInfo.className = 'file-info success';
}

function showFileError(message) {
    fileInfo.textContent = `✗ ${message}`;
    fileInfo.className = 'file-info error';
}

// Toggle advanced options
function toggleAdvancedOptions() {
    const isHidden = optionsContainer.style.display === 'none';
    optionsContainer.style.display = isHidden ? 'grid' : 'none';
    advancedToggle.classList.toggle('active');
}

// Get user options
function getConversionOptions() {

    return {

        margin: {
            top: parseFloat(marginTopInput.value),
            bottom: parseFloat(marginBottomInput.value),
            left: parseFloat(marginLeftInput.value),
            right: parseFloat(marginRightInput.value)
        },

        pageOrientation: pageOrientationSelect.value,
        pageSize: pageSizeSelect.value,

        compressImages: compressImagesCheckbox.checked
    };
}

function readFileAsArrayBuffer(file) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();

        reader.onload = function(e) {
            resolve(e.target.result);
        };

        reader.onerror = function(error) {
            reject(error);
        };

        reader.readAsArrayBuffer(file);
    });
}

// Main conversion handler
async function handleConversion() {

    try {

        console.log("Starting conversion");

        if (!selectedFile) {
            alert("Select a file first");
            return;
        }

        const arrayBuffer =
            await readFileAsArrayBuffer(selectedFile);

        console.log("File loaded");

        const options =
            getConversionOptions();

        const previewContainer =
            document.getElementById("docPreview");

        console.log("Preview container:", previewContainer);

        previewContainer.innerHTML = "";

        await docx.renderAsync(
            arrayBuffer,
            previewContainer
        );

        const docSection =
    previewContainer.querySelector("section.docx");

const rawText = docSection.innerText;

if (
    rawText.includes("##") ||
    rawText.includes("**")
) {

    docSection.innerHTML =
        marked.parse(rawText);
}

        console.log(previewContainer.innerHTML.length);
console.log(previewContainer.innerHTML.substring(0, 200));
console.log("scrollHeight:", previewContainer.scrollHeight);
console.log("offsetHeight:", previewContainer.offsetHeight);

const docPage = previewContainer.querySelector("section.docx");

console.log(docPage);
console.log(docPage?.scrollHeight);
        console.log("DOCX rendered");

        await generatePdfFromElement(
            previewContainer,
            options
        );
        console.log(
    "Blob:",
    convertedPdfBlob
);

console.log(
    "Blob size:",
    convertedPdfBlob?.size
);

        showStatus('Conversion complete!', 100);

downloadSection.style.display = 'block';

convertBtn.disabled = false;

        console.log("PDF generated");

    }
    catch (err) {

        console.error(err);

        alert(err.message);

    }

}


// Generate PDF from HTML
function generatePdfFromElement(element, options) {

    return new Promise(async (resolve, reject) => {

        try {

            const docElement =
                element.querySelector("section.docx");

            if (!docElement) {
                throw new Error(
                    "DOCX content not found"
                );
            }

            console.log("Generating PDF from:", docElement);

            await new Promise(r =>
                setTimeout(r, 500)
            );

            const worker = html2pdf()
                .set({

                    margin: [
                        options.margin.top * 10,
                        options.margin.right * 10,
                        options.margin.bottom * 10,
                        options.margin.left * 10
                    ],

                    filename:
                        `${selectedFile.name.split('.')[0]}.pdf`,

                    image: {
                        type: 'jpeg',
                        quality: 1
                    },

                    html2canvas: {
                        scale: 2,
                        useCORS: true,
                        allowTaint: true,
                        backgroundColor: '#ffffff',
                        logging: true
                    },

                    jsPDF: {
                        unit: 'mm',
                        format: options.pageSize,
                        orientation:
                            options.pageOrientation
                    }
                })
                .from(docElement);

            const pdfBlob =
                await worker.outputPdf('blob');

            console.log(
                "PDF blob size:",
                pdfBlob.size
            );

            convertedPdfBlob = pdfBlob;

            resolve();

        }
        catch (err) {

            console.error(err);

            reject(err);

        }

    });

}
// Show status message
function showStatus(message, progress) {
    statusSection.style.display = 'block';
    statusMessage.textContent = message;
    progressFill.style.width = progress + '%';
}

// Download PDF
function downloadPdf() {
    if (!convertedPdfBlob) {
        alert('No PDF available for download');
        return;
    }

    const url = URL.createObjectURL(convertedPdfBlob);
    const link = document.createElement('a');
    link.href = url;
    link.download = `${selectedFile.name.split('.')[0]}.pdf`;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    URL.revokeObjectURL(url);
}

// Reset form
function resetForm() {
    selectedFile = null;
    convertedPdfBlob = null;
    wordFileInput.value = '';
    fileInfo.textContent = '';
    fileInfo.className = 'file-info';
    convertBtn.disabled = true;
    downloadSection.style.display = 'none';
    statusSection.style.display = 'none';
    optionsContainer.style.display = 'none';
    advancedToggle.classList.remove('active');

    // Reset options to defaults
    marginTopInput.value = '1';
    marginBottomInput.value = '1';
    marginLeftInput.value = '1';
    marginRightInput.value = '1';
    pageOrientationSelect.value = 'portrait';
    pageSizeSelect.value = 'A4';
    compressImagesCheckbox.checked = false;
}

// Initialize
console.log('Word to PDF Converter initialized successfully');