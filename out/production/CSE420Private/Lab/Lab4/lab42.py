list1 = ["public","private","protected","final","public","static"]
list2 = ["void","int","String","float","bool"]

def main():

    f = open("input.txt", "r")

    if f.mode == 'r':
        input = f.read()
    list = input.split('\n')

    for i in range(len(list)):
        newlist = list[i].split()

        fb = list[i].find("(")
        lb = list[i].find(")")

        if (fb != -1 and lb != -1):
            for x in range(len(newlist)):
                if (newlist[x] in list1):
                    if (newlist[x + 1] in list2):
                        for y in range(x + 2, len(newlist)):
                            print(newlist[y], end=' ')
                        print("return type: ", newlist[x + 1])

if __name__ == "__main__":
    main()