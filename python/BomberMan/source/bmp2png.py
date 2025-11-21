from PIL import Image
import os

# 変換したいフォルダ
folder = "../images/"

for filename in os.listdir(folder):
    if filename.lower().endswith(".bmp"):
        input_path = os.path.join(folder, filename)

        # 拡張子を png に変更
        output_name = filename[:-4] + ".png"
        output_path = os.path.join(folder, output_name)

        print("変換中:", input_path)

        img = Image.open(input_path).convert("RGBA")
        datas = img.getdata()

        new_data = []
        for pixel in datas:
            # 黒を透過
            if pixel[0] < 10 and pixel[1] < 10 and pixel[2] < 10:
                new_data.append((0, 0, 0, 0))
            else:
                new_data.append(pixel)

        img.putdata(new_data)
        img.save(output_path, "PNG")

        print("→ 変換完了:", output_path)

print("すべての BMP の変換が完了しました")
