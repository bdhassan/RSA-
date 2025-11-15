# RSA

This project implements a basic RSA cryptosystem in Java.
It allows the user to:

1)Generate RSA public and private keys
2)Encrypt a plaintext file
3)Decrypt the encrypted output back to the original file
3)The implementation uses Java’s BigInteger library to handle large prime numbers and modular arithmetic.

**Features**
- 2048-bit Prime Generation
- Uses BigInteger.probablePrime() to generate two large primes.

**RSA Key Construction**
Computes:

-N = p × q
-φ(N) = (p − 1)(q − 1)
-Public exponent p (random prime)
-Private exponent s = p⁻¹ mod φ(N)

**Key Storage**
Keys are stored in:

pubkey.dat → Public key (p, N)
privkey.dat → Private key (s, N)

**File Encryption**

Reads Test.txt in 32-byte blocks and converts each block into a BigInteger using base 128.
Performs encryption using:

C = M^p mod N

Writes:

-Block size
-Encrypted BigInteger

To: EncryptedTest.dat

**How To Run**

1) Compile: javac RSA.java
2) Run: java RSA
3) Program Flow:
   Press Enter to generate keys...
   Press Enter to encrypt the file...
   Press Enter to decrypt the file...

- Ensure that Test.txt exists before starting encryption

  **Generated Files:**

- pubkey.dat:	Contains RSA public key (p, N)
- privkey.dat:	Contains RSA private key (s, N)
- EncryptedTest.dat:	Binary file containing encrypted blocks
- DecryptedTest.txt:	Output after decryption

**Important Notes / Limitations**

- This implementation is educational, not secure for real-world cryptography.
- Uses a random prime as public exponent, not the standard 65537.
- No padding (like OAEP) → vulnerable to cryptographic attacks.
- Reads and encodes blocks manually using base-128.

