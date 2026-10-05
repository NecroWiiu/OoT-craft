"""
Builds a LOCAL-ONLY Minecraft resource pack from textures extracted from YOUR OWN SM64 ROM.
Never share the resulting zip: it contains Nintendo assets.

Step 1 (WSL/Linux): build the n64decomp/sm64 repo with your ROM as baserom.us.z64.
        The build extracts the textures as .png files into the repo folders.
Step 2: scan what was extracted:
        python make_sm64_pack.py scan  <path-to-sm64-repo>
Step 3: fill in sm64_mapping.json (source png -> Minecraft texture path), then:
        python make_sm64_pack.py build <path-to-sm64-repo> <output.zip>
        Put the zip in <prism instance>/.minecraft/resourcepacks/ and enable it in-game.
Needs: pip install pillow
"""
import io
import json
import sys
import zipfile
from collections import Counter
from pathlib import Path

from PIL import Image

PACK_FORMAT = 15  # Minecraft 1.20.1


def scan(repo: Path):
    pngs = list(repo.rglob("*.png"))
    counts = Counter(p.relative_to(repo).parts[0] for p in pngs)
    print(f"{len(pngs)} png files found")
    for k, v in counts.most_common():
        print(f"  {k}: {v}")
    out = Path("sm64_png_list.txt")
    out.write_text("\n".join(p.relative_to(repo).as_posix() for p in sorted(pngs)))
    print(f"Full list written to {out.resolve()}")


def build(repo: Path, out_zip: Path):
    mapping = json.loads((Path(__file__).parent / "sm64_mapping.json").read_text())
    with zipfile.ZipFile(out_zip, "w", zipfile.ZIP_DEFLATED) as z:
        z.writestr("pack.mcmeta", json.dumps({"pack": {
            "pack_format": PACK_FORMAT, "description": "SM64 Mode (local use only)"}}))
        n = 0
        for entry in mapping["copy"]:
            src = repo / entry["from"]
            if not src.exists():
                print("missing:", entry["from"])
                continue
            img = Image.open(src).convert("RGBA")
            scale = int(entry.get("scale", 1))
            if scale > 1:
                img = img.resize((img.width * scale, img.height * scale), Image.NEAREST)
            buf = io.BytesIO()
            img.save(buf, "PNG")
            z.writestr(entry["to"], buf.getvalue())
            n += 1
    print(f"Wrote {n} textures to {out_zip}")


if __name__ == "__main__":
    if len(sys.argv) >= 3 and sys.argv[1] == "scan":
        scan(Path(sys.argv[2]))
    elif len(sys.argv) >= 4 and sys.argv[1] == "build":
        build(Path(sys.argv[2]), Path(sys.argv[3]))
    else:
        print(__doc__)
