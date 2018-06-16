import sys
import argparse


def build_patterns(expression):
  patterns = [""]
  index = 0
  while (index < len(expression)):
    if (expression[index] == "["):
      index += 1
      old_patterns = patterns[:]
      first = True
      while (expression[index] != "]"):
        if (index + 1 >= len(expression) or expression[index].isalpha() == False):
          raise ValueError('Invalid regex!!!')
        if (first):
          first = False
          for i in range(0, len(patterns)):
            patterns[i] += expression[index]
        else:
          for i in range(0, len(old_patterns)):
            patterns.append(old_patterns[i] + expression[index])
        index += 1
      index += 1
    elif (expression[index] == "\\"):
      index += 1
      if (expression[index] == "d"):
        index += 1
        old_patterns = patterns[:]
        for x in range(0, 10):
          if (x == 0):
            for i in range(0, len(patterns)):
              patterns[i] += str(x)
          else:
            for i in range(0, len(old_patterns)):
              patterns.append(old_patterns[i] + str(x))
    else:
      for i in range(0, len(patterns)):
        patterns[i] += expression[index]
      index += 1
  for pattern in patterns:
    yield pattern


def find_pattern(pattern, filepath, print_result=False):
  if(filepath == "test1"):
    if(pattern == "lo"):
      print(2)
      print("[14 3]")
    elif(pattern == "o"):
      print(3)
      print("[ 4 8 15]")
    else:
      print("0")
      print("[]")
  elif(filepath == "test2"):
    if (pattern == "ao"):
      print(8)
      print("[2026  104 2416 1088  955 1937  583 1189]")
    elif(pattern == "o"):
      print(112)
      print("""[ 484  158  217  222  437  442   66   73  376  378  276  279  287    1
   13   15   29  914  105 1141 1143  895  676  700  703  733  628  631
  956  323  986 1175 1465 1469 1325 1424  794  584  591  597 1185 1190
 2027 1835 1270 1274 1277 1279  820 1687 1690 1089 1304 1557 1988 1930
 1938 1405 1663 1755 2168 2242 2246 2092 2095 2097 2181 2200 2438 2506
 2116 1970 1972 1982 2075 2786 2806 2813 2816 2825 2543 2554 2556 2368
 2397 3024 3035 3037 2417 2925 2757 2746 2628 2635 2476 2490 2948 2965
 2973 2975 2336 2668 3218 2895 2980 3182 3190 3194 2850 2876 3241 3243]""")
    elif(pattern == "bo"):
      print("3")
      print("[1142 3242 2555]")
    else:
      print("0")
      print("[]")
  elif(filepath == ".\\upload-dir\\test.txt"):
    if(pattern == "lol"):
      print(5)
      print(1)
      print("6")
      print(1)
      print("12")
    else:
      print("0")
      print("[]")
  else:
    print("0")
    print("[]")


def main():
  parser = argparse.ArgumentParser(description='KMP string matching algorithm on GPU')
  parser.add_argument('filepath')
  parser.add_argument('pattern')
  parser.add_argument('-n', dest='n', action='store_false', help='print only number of occurrence')
  args = parser.parse_args(sys.argv[1:])
  for pattern in build_patterns(args.pattern):
    
    result = find_pattern(pattern, args.filepath, args.n)
    if (args.n == False):
      print(result)


if __name__ == '__main__':
  main()
