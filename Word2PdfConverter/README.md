# Tools - Word to PDF Converter

A modern, single-page web application that converts Word documents (.docx) to PDF with advanced formatting options.

## Features

- **Easy File Upload**: Simple drag-and-drop interface for selecting Word documents
- **Advanced Formatting Options**:
  - Font family and size customization
  - Custom margins and padding
  - Page orientation and size settings
  - Image compression
  - Preserve original formatting option

- **Real-time Conversion Progress**: Visual progress bar during conversion
- **Instant Download**: Download converted PDF with one click
- **Responsive Design**: Works seamlessly on desktop and mobile devices
- **No Server Required**: Client-side conversion using Mammoth and html2pdf libraries

## Technologies Used

- **Frontend**: HTML5, CSS3, Vanilla JavaScript
- **Libraries**:
  - [Mammoth.js](https://github.com/mwilson/mammoth.js) - Word document conversion
  - [html2pdf.js](https://ekoopmans.github.io/html2pdf.js/) - HTML to PDF conversion

## Getting Started

1. Clone or download this repository
2. Open `index.html` in your web browser
3. Select a Word file (.docx)
4. (Optional) Configure advanced options
5. Click "Convert to PDF"
6. Download the generated PDF

## File Structure

```
Tools/
├── index.html      # Main HTML file
├── styles.css      # CSS styling
├── script.js       # JavaScript functionality
└── README.md       # Documentation
```

## How It Works

1. **File Selection**: User selects a .docx file from their computer
2. **Validation**: File type and size are validated
3. **Conversion**:
   - Mammoth.js extracts content from Word document
   - HTML is generated with custom styling
   - html2pdf converts HTML to PDF with user-specified options
4. **Download**: User can download the resulting PDF

## Supported Options

### Font Settings
- Arial, Times New Roman, Courier New, Calibri, Verdana, Georgia
- Font sizes from 8pt to 72pt

### Page Settings
- Margins: 0-2 inches (top, bottom, left, right)
- Padding: 0-50 pixels
- Orientation: Portrait or Landscape
- Page sizes: A4, Letter, Legal

### Additional Options
- Preserve original formatting
- Compress images

## Browser Compatibility

- Chrome/Edge: ✅ Full support
- Firefox: ✅ Full support
- Safari: ✅ Full support
- Internet Explorer: ❌ Not supported

## Limitations

- Maximum file size: 10 MB
- Supported formats: .docx (Word 2007 and later), .doc (limited support)
- Complex VBA macros are not preserved
- Some advanced Word formatting may be simplified

## Future Enhancements

- [ ] Batch conversion for multiple files
- [ ] Support for additional document formats (.odt, .rtf)
- [ ] Watermark support
- [ ] OCR for scanned documents
- [ ] Cloud storage integration
- [ ] Conversion history

## Customization

You can customize the tool by:
1. Modifying the header and footer in `index.html`
2. Adjusting colors in the `:root` CSS variables in `styles.css`
3. Adding new formatting options in `script.js`

## License

This project is open source and available under the MIT License.

## Support

For issues, suggestions, or contributions, please create an issue or submit a pull request.

---

**Note**: This tool runs entirely in your browser. No data is sent to any server.