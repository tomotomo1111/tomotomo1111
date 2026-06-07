def main():
    width = 64
    height = 64
    with open("./m.pgm") as f:
        f.write("1\n")
        f.write("255\n")
        for i in range(height):
            for j in range(width):
                print(i + j, file=f)
            print("\n", file=f)