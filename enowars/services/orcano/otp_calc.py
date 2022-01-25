import sys
import os
import struct

from Crypto.Cipher import ChaCha20

def main():
	if len(sys.argv) != 3:
		print("{} <otp_init output> <otp_sync output>")
		return

	otp_init_arg = sys.argv[1]
	otp_sync_arg = sys.argv[2]

	otp_init = list(reversed([int(x[1:]) for x in otp_init_arg.strip().split(" ")]))
	otp_sync = list(reversed([int(x[1:]) for x in otp_sync_arg.strip().split(" ")]))

	otp_key = b""
	for s in otp_init[1:9]:
		otp_key += struct.pack(">l", s)
	otp_nonce = b""
	for s in otp_init[9:11]:
		otp_nonce += struct.pack(">l", s)

	gen = ChaCha20.new(key=otp_key, nonce=otp_nonce)
	gen.seek(otp_sync[2] * 8)
	prev_otp0, prev_otp1 = struct.unpack_from(">ll", gen.encrypt(b"\x00" * 8), 0x0)
	if prev_otp0 != otp_sync[0] or prev_otp1 != otp_sync[1]:
		print("validation mismatch")
		return
	next_otp0, next_otp1 = struct.unpack_from(">ll", gen.encrypt(b"\x00" * 8), 0x0)
	print(":i{}:i{}".format(next_otp0, next_otp1))

if __name__ == "__main__":
	main()