package maaartin.math;

import static com.google.common.base.Preconditions.checkArgument;

import lombok.Getter;

/**
 * This class provides common modular arithmetic.
 * Results of all methods are ints guaranteed to be non-negative and less then modulus.
 *
 * <p>The method names were chosen to be as short as {@code Math.pow}.
 */
public final class IntModulus {
	private IntModulus(int modulus) {
		this.modulus = modulus;
	}

	@SuppressWarnings("boxing") public static IntModulus newModulus(int modulus) {
		checkArgument(modulus>0, "Modulus must be positive, got %s", modulus);
		return new IntModulus(modulus);
	}

	public int pow(long base, long exp) {
		checkArgument(exp>=0, "Only non-negative exponents are implemented.");  //TODO allow negative exponents
		if (modulus==1) return 0;
		if (exp==0) return 1;
		return powInternal(mod(base), exp);
	}

	private int powInternal(int base, long exp) {
		assert base>=0;
		if (base<=1) return base; // For both 0 and 1, no exponentiation is needed.

		// See https://en.wikipedia.org/wiki/Modular_exponentiation#Right-to-left_binary_method
		int result = 1;
		for (int x=base; exp>0; exp>>=1) {
			if ((exp&1) != 0) result = mul(result, x);
			x = square(x);
		}
		return result;
	}

	private int square(int x) {
		final long x2 = x;
		return (int) ((x2 * x2) % modulus); // The cast is safe and the result is surely non-negative.
	}

	/** Return a non-negative value less than modulus and congruent to the exact product. */
	public int mul(long x, long y) {
		return mul(mod(x), mod(y));
	}

	/** Return a non-negative value less than modulus and congruent to the exact product. */
	public int mul(int x, int y) {
		return mod((long) x * y);
	}

	/** Return a non-negative value less than modulus and congruent to the exact sum. */
	public int add(int x, int y) {
		return mod((long) x + y);
	}

	/** Return a non-negative value less than modulus and congruent to the exact difference. */
	public int sub(int x, int y) {
		return mod((long) x - y);
	}

	/** Return a non-negative value less than modulus and congruent to the operand. */
	public int mod(long x) {
		return fixMod((int) (x % modulus)); // As modulus is an int, the cast is safe.
	}

	/** Return a non-negative value less than modulus and congruent to the operand. */
	public int mod(int x) {
		return fixMod(x % modulus);
	}

	private int fixMod(int result) {
		return result<0 ? result+modulus : result;
	}

	@Getter private final int modulus;
}
