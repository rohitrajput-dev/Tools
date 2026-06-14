# Word2PdfConverter

A modern, single-page web application that converts Word documents (.docx) to PDF with advanced formatting options. Built with vanilla JavaScript, HTML5, and CSS3.

## Features

- **Easy File Upload**: Simple interface for selecting Word documents (.docx, .doc)
- **Advanced Formatting Options**:
  - Custom margins (0-2 inches) for top, bottom, left, and right
  - Page orientation (Portrait/Landscape)
  - Multiple page sizes (A4, Letter, Legal)
  - Image compression options
  - Real-time document preview
  
- **Real-time Conversion Progress**: Visual progress bar during conversion
- **Instant Download**: Download converted PDF with one click
- **Responsive Design**: Works seamlessly on desktop and mobile devices
- **No Server Required**: Client-side conversion using external libraries
- **Markdown Support**: Automatic markdown parsing in converted documents

## Technologies Used

- **Frontend**: HTML5, CSS3, Vanilla JavaScript
- **Libraries**:
  - [docx-preview](https://unpkg.com/docx-preview) - Word document rendering and preview
  - [html2pdf.js](https://cdnjs.cloudflare.com/ajax/libs/html2pdf.js/) - HTML to PDF conversion
  - [jszip](https://cdnjs.cloudflare.com/ajax/libs/jszip/) - ZIP file handling for .docx format
  - [marked.js](https://cdn.jsdelivr.net/npm/marked/) - Markdown parsing and rendering

## File Structure

```
Word2PdfConverter/
├── index.html      # Main HTML file with UI structure
├── styles.css      # Complete styling and responsive design
├── script.js       # Core conversion logic and event handlers
└── README.md       # This documentation file
```

## File Details

### index.html
- Header with navigation menu
- File upload section with drag-and-drop support
- Advanced options toggle with margin, page, and additional settings
- Status section with progress bar
- Action buttons (Convert, Reset)
- Download section
- Footer with links
- External CDN libraries loaded in correct order

### styles.css
- Root CSS variables for colors and shadows
- Complete styling for all UI components
- Responsive design breakpoints (768px, 480px)
- Smooth transitions and animations
- Mobile-first approach
- Gradient backgrounds and hover effects
- Progress bar animation with pulse effect

### script.js
Main functionality includes:
- File validation (type and size)
- Conversion options management
- DOCX file reading and rendering
- HTML to PDF generation
- Progress tracking
- Download functionality
- Form reset and state management

## Getting Started

1. **Clone or download** this repository
2. **Open `index.html`** in your web browser
3. **Select a Word file** (.docx or .doc format)
4. **(Optional) Configure** advanced formatting options
5. **Click "Convert to PDF"** to start conversion
6. **Download** the generated PDF file

## How It Works

### Conversion Process
1. **File Selection & Validation**
   - User selects .docx/.doc file
   - File type is validated (MIME type check)
   - File size is limited to 10MB maximum

2. **Document Rendering**
   - Mammoth/DOCX library reads the file as ArrayBuffer
   - Content is rendered in preview container
   - Markdown syntax (## and **) is automatically parsed if detected

3. **PDF Generation**
   - html2pdf.js converts rendered HTML to PDF
   - User-specified margins and page settings are applied
   - Image quality and compression settings are processed

4. **Download**
   - PDF blob is generated and offered for download
   - Original filename is preserved (with .pdf extension)

## Configuration Options

### Margin Settings (inches)
- Top: 0-2 inches (default: 1)
- Bottom: 0-2 inches (default: 1)
- Left: 0-2 inches (default: 1)
- Right: 0-2 inches (default: 1)

### Page Settings
- **Orientation**: Portrait (default) or Landscape
- **Page Size**: A4 (default), Letter, or Legal

### Additional Options
- **Compress Images**: Checkbox to enable image compression during conversion

## Browser Compatibility

| Browser | Support |
|---------|---------|
| Chrome/Edge | ✅ Full support |
| Firefox | ✅ Full support |
| Safari | ✅ Full support |
| Internet Explorer | ❌ Not supported |

## Limitations

- **Maximum file size**: 10 MB
- **Supported formats**: .docx (Word 2007 and later), .doc (limited support)
- **Complex formatting**: Some advanced Word formatting may be simplified
- **VBA macros**: Not preserved during conversion
- **Tables and images**: May require adjustment of compression settings

## Customization Guide

### Modify Colors
Edit CSS variables in `styles.css`:
```css
:root {
    --primary-color: #2563eb;
    --secondary-color: #64748b;
    --success-color: #22c55e;
    /* ... other colors */
}
```

### Update Header/Footer
Edit the `<header>` and `<footer>` sections in `index.html`:
- Customize company name, logo, and navigation links
- Modify footer content and links

### Add Formatting Options
To add new conversion options:
1. Add HTML input in `index.html` (in `.options-container`)
2. Add CSS styling in `styles.css`
3. Add JavaScript logic in `script.js` (`getConversionOptions()` function)

### Change Default Values
In `script.js`, modify the `resetForm()` function:
```javascript
marginTopInput.value = '1';  // Change default margins
pageOrientationSelect.value = 'portrait';  // Change default orientation
```

## Future Enhancements

- [ ] Batch conversion for multiple files
- [ ] Support for additional document formats (.odt, .rtf, .txt)
- [ ] Watermark and header/footer text support
- [ ] OCR capabilities for scanned documents
- [ ] Cloud storage integration (Google Drive, OneDrive)
- [ ] Conversion history and settings presets
- [ ] Advanced layout preservation options
- [ ] Password protection for PDFs
- [ ] Custom font selection

## Troubleshooting

### File Not Converting
- Verify the file is in .docx format
- Check file size is under 10 MB
- Ensure all external CDN libraries have loaded

### PDF Quality Issues
- Try adjusting the image compression setting
- Verify page size and orientation are correct
- Check browser console for errors (F12)

### Download Not Working
- Check browser download settings
- Ensure pop-ups are not blocked
- Clear browser cache and try again

## Performance Notes

- Conversion speed depends on file size and browser performance
- Larger files may take several seconds to process
- Document preview rendering occurs before PDF generation

## License

This project is open source and available under the MIT License.

## Support & Contribution

For issues, suggestions, or contributions:
1. Create an issue with a detailed description
2. Submit a pull request with improvements
3. Share feedback on features or usability

---

**Privacy Notice**: This tool runs entirely in your browser. No data is sent to any server. Your documents remain completely private and secure on your device.
