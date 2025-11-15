import java.util.Random;
import java.math.BigInteger;
import java.util.*;
import java.io.*;

public class RSA {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		try { 
			System.out.println("Press Enter to generate keys...");
			scanner.nextLine();
			makeKeys();

			System.out.println("\nPress Enter to encrypt the file...");
			scanner.nextLine();
			encrypt();

			System.out.println("\nPress Enter to decrypt the file...");
			scanner.nextLine();
			decrypt();
		
			System.out.println("\nAll operations completed");
		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
	}

	
	// ----- Key Generation -----
	public static void makeKeys() throws Exception {
		Random rand = new Random();

		System.out.println("Generating two 2048-bit primes");
		BigInteger prime1 = BigInteger.probablePrime(2048, rand);
		BigInteger prime2 = BigInteger.probablePrime(2048, rand);
		
		BigInteger N = prime1.multiply(prime2);
		BigInteger phi = (prime1.subtract(BigInteger.ONE)).multiply(prime2.subtract(BigInteger.ONE));
		BigInteger p = BigInteger.probablePrime(2048, rand);	
		BigInteger s = p.modInverse(phi);

		System.out.println("Public key (p,N):");
		System.out.println("- - - - - - - - - -");
    		System.out.println("p = " + p);
		System.out.println("- - - - - - - - - -");
    		System.out.println("N = " + N);
		System.out.println("- - - - - - - - - -");
    		System.out.println("Private key (s,N):");
		System.out.println("- - - - - - - - - -");
    		System.out.println("s = " + s);
		System.out.println("- - - - - - - - - -");
    		System.out.println("N = " + N);

    
    		try (ObjectOutputStream pubOut = new ObjectOutputStream(new FileOutputStream("pubkey.dat")); 
			ObjectOutputStream privOut = new ObjectOutputStream(new FileOutputStream("privkey.dat"))) {
    		
			pubOut.writeObject(p);
			pubOut.writeObject(N);
	    
			privOut.writeObject(s);
			privOut.writeObject(N);
		}
	}

	// - - - - - Encryption - - - - - 
	public static void encrypt() throws Exception {
		try (ObjectInputStream pubIn = new ObjectInputStream(new FileInputStream("pubkey.dat"));
			FileInputStream fileIn = new FileInputStream("Test.txt");
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("EncryptedTest.dat"))) {

			BigInteger p = (BigInteger) pubIn.readObject();
			BigInteger N = (BigInteger) pubIn.readObject();
		
			byte[] buffer = new byte[32];
			int n;

			while((n = fileIn.read(buffer)) != -1) {
				BigInteger M = BigInteger.ZERO;
				for (int i = 0; i < n; i++) {
					M = M.multiply(BigInteger.valueOf(128)).add(BigInteger.valueOf(Byte.toUnsignedInt(buffer[i])));
					
				}

				BigInteger C = M.modPow(p, N);
				out.writeInt(n);
				out.writeObject(C);
			}
		}
	}
	
	// ----- Decryption -----
	public static void decrypt() throws Exception {

		try (ObjectInputStream privIn = new ObjectInputStream( new FileInputStream("privkey.dat"));
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("EncryptedTest.dat"));
			FileOutputStream fileOut = new FileOutputStream("DecryptedTest.txt")) {

			BigInteger s = (BigInteger) privIn.readObject();
			BigInteger N = (BigInteger) privIn.readObject();
			
			while(true) {
				try {
					int blockSize = in.readInt();
					BigInteger C = (BigInteger) in.readObject();
					BigInteger M = C.modPow(s,N);

					byte[] block = new byte[blockSize];
					for(int i = blockSize - 1; i >= 0; i--) {
						block[i] = (byte) (M.mod(BigInteger.valueOf(128)).intValue());
						M = M.divide(BigInteger.valueOf(128));
					}

					fileOut.write(block);
				} catch (EOFException eof) {
					break;
				}
			}
		}	
	}
}	

