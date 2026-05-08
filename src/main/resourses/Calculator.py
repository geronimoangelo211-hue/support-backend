#Python Language

#Input Section
num1 = int(input("Enter an number: "))         # int 
num2 = float(input("Enter a second number with decimal number: ")) # float 
operator = input("Enter an operator (+, -, *, /): ")  # char 

#Processing, Section
if operator == '+':                   
    result = num1 + num2           #+ operator
elif operator == '-':
    result = num1 - num2           #- operator
elif operator == '*':
    result = num1 * num2           #* operator
elif operator == '/':
    result = num1 / num2           #/ operator
else:
    result = "Invalid input!!!" #if user put not on the listed, it will be error

#Output Section
print(num1, operator, num2, "=", result)