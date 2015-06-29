package maaartin.math;

import java.math.BigInteger;
import java.util.Random;

import junit.framework.TestCase;

public class IntModulusTest extends TestCase {
	public void testPow() {
		final Random random = TestHelper.newRandom();
		for (final int m : MODULI) {
			final IntModulus modulus = IntModulus.newModulus(m);
			for (final long base : TestHelper.testingLongs(200, random, true)) {
				for (final long exp : TestHelper.testingLongs(200, random, false)) {
					final long actual = modulus.pow(base, exp);
					final long expected = toBI(base).modPow(toBI(exp), toBI(m)).longValue();
					if (expected==actual) continue;
					assertEquals(expected, actual);
				}
			}
		}
	}

	public void testMul_Long() {
		final Random random = TestHelper.newRandom();
		for (final int m : MODULI) {
			final IntModulus modulus = IntModulus.newModulus(m);
			for (final long x : TestHelper.testingLongs(1000, random, true)) {
				for (final long y : TestHelper.testingLongs(1000, random, true)) {
					final long actual = modulus.mul(x, y);
					final long expected = toBI(x).multiply(toBI(y)).mod(toBI(m)).longValue();
					assertEquals(expected, actual);
				}
			}
		}
	}

	public void testMul_Int() {
		final Random random = TestHelper.newRandom();
		for (final int m : MODULI) {
			final IntModulus modulus = IntModulus.newModulus(m);
			for (final int x : TestHelper.testingInts(1000, random, true)) {
				for (final int y : TestHelper.testingInts(1000, random, true)) {
					final long actual = modulus.mul(x, y);
					final long expected = toBI(x).multiply(toBI(y)).mod(toBI(m)).longValue();
					assertEquals(expected, actual);
				}
			}
		}
	}

	public void testAdd() {
		final Random random = TestHelper.newRandom();
		for (final int m : MODULI) {
			final IntModulus modulus = IntModulus.newModulus(m);
			for (final int x : TestHelper.testingInts(2000, random, true)) {
				for (final int y : TestHelper.testingInts(2000, random, true)) {
					final long actual = modulus.add(x, y);
					final long expected = toBI(x).add(toBI(y)).mod(toBI(m)).longValue();
					assertEquals(expected, actual);
				}
			}
		}
	}

	public void testSub() {
		final Random random = TestHelper.newRandom();
		for (final int m : MODULI) {
			final IntModulus modulus = IntModulus.newModulus(m);
			for (final int x : TestHelper.testingInts(2000, random, true)) {
				for (final int y : TestHelper.testingInts(2000, random, true)) {
					final long actual = modulus.sub(x, y);
					final long expected = toBI(x).subtract(toBI(y)).mod(toBI(m)).longValue();
					assertEquals(expected, actual);
				}
			}
		}
	}

	public void testMod_Long() {
		final Random random = TestHelper.newRandom();
		for (final int m : TestHelper.testingInts(4000, random, false)) {
			if (m==0) continue;
			final IntModulus modulus = IntModulus.newModulus(m);
			for (final long x : TestHelper.testingLongs(4000, random, true)) {
				final long actual = modulus.mod(x);
				final long expected = toBI(x).mod(toBI(m)).longValue();
				assertEquals(expected, actual);
			}
		}
	}

	public void testMod_Int() {
		final Random random = TestHelper.newRandom();
		for (final int m : TestHelper.testingInts(4000, random, false)) {
			if (m==0) continue;
			final IntModulus modulus = IntModulus.newModulus(m);
			for (final int x : TestHelper.testingInts(4000, random, true)) {
				final long actual = modulus.mod(x);
				final long expected = toBI(x).mod(toBI(m)).longValue();
				assertEquals(expected, actual);
			}
		}
	}

	private BigInteger toBI(long x) {
		return BigInteger.valueOf(x);
	}

	private static final int[] MODULI = {3, 15, 17, 257, 1<<16, Integer.MAX_VALUE};
}
