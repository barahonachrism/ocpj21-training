#!/bin/bash

# Add TeX bin to PATH
export PATH="/Library/TeX/texbin:$PATH"

# Define the output file
OUTPUT_FILE="ocpj21-book.pdf"

# Define the list of files in order
FILES=(
    "intro.md"
    "ch01.md" "ch01a.md"
    "ch02.md" "ch02a.md"
    "ch03.md" "ch03a.md"
    "ch04.md" "ch04a.md"
    "ch05.md" "ch05a.md"
    "ch06.md" "ch06a.md"
    "ch07.md" "ch07a.md"
    "ch08.md" "ch08a.md"
    "ch09.md" "ch09a.md"
    "ch10.md" "ch10a.md"
    "ch11.md" "ch11a.md"
    "ch12.md" "ch12a.md"
    "ch13.md" "ch13a.md"
    "ch14.md" "ch14a.md"
)

# Check if pandoc is installed
if ! command -v pandoc &> /dev/null; then
    echo "Pandoc could not be found. Please install it."
    exit 1
fi

# Check if pdflatex is installed
if ! command -v pdflatex &> /dev/null; then
    echo "pdflatex could not be found. Please install BasicTeX."
    exit 1
fi

# Generate the PDF
echo "Generating PDF..."

# Create a temporary directory for processed files
mkdir -p processed_files

# Preprocess files
PROCESSED_FILES=()
for file in "${FILES[@]}"; do
    python3 preprocess.py "$file" > "processed_files/$file"
    PROCESSED_FILES+=("processed_files/$file")
done

# We use --from markdown+yaml_metadata_block to ensure it parses metadata
# We use xelatex for better Unicode support (box drawing characters)
# We specify a mono font that supports these characters
# We include metadata.yaml first to define the Book Title
pandoc "metadata.yaml" "${PROCESSED_FILES[@]}" -o "$OUTPUT_FILE" --pdf-engine=xelatex --toc --variable margin-left=1in --variable margin-right=1in --variable margin-top=1in --variable margin-bottom=1in --variable monofont="Menlo"

# Clean up
rm -rf processed_files

if [ $? -eq 0 ]; then
    echo "PDF generated successfully: $OUTPUT_FILE"
else
    echo "Error generating PDF."
    exit 1
fi
