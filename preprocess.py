import sys
import re

def preprocess(filename):
    with open(filename, 'r', encoding='utf-8') as f:
        content = f.read()

    # Regex to find YAML frontmatter
    # We look for --- at the start, some content, and then --- or *** (since we changed them)
    # But wait, we changed the inner --- to ***, so the frontmatter should still be closed by ---
    # However, let's be robust.
    
    frontmatter_match = re.match(r'^---\s*\n(.*?)\n---\s*\n', content, re.DOTALL)
    
    if frontmatter_match:
        frontmatter = frontmatter_match.group(1)
        body = content[frontmatter_match.end():]
        
        # Extract title and subtitle
        title_match = re.search(r'^title:\s*"(.*?)"', frontmatter, re.MULTILINE)
        subtitle_match = re.search(r'^subtitle:\s*"(.*?)"', frontmatter, re.MULTILINE)
        
        title = title_match.group(1) if title_match else None
        subtitle = subtitle_match.group(1) if subtitle_match else None
        
        new_content = ""
        if title:
            new_content += f"# {title}\n\n"
        if subtitle:
            new_content += f"## {subtitle}\n\n"
            
        new_content += body
        return new_content
    else:
        return content

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Usage: python3 preprocess.py <filename>")
        sys.exit(1)
        
    filename = sys.argv[1]
    print(preprocess(filename))
