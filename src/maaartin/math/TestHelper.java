package maaartin.math;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Random;
import java.util.Set;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

public class TestHelper {
	/**
	 * Return a new Random seeded using the provided seeds and also the caller's class name and method name.
	 * The caller is defined as the first caller outside of this class.
	 *
	 * <p>This is hacky but needed in order to make pseudo-random testing more efficient by ensuring
	 * that similar tests use different seeds.
	 */
	static Random newRandom(long... seeds) {
		final StackTraceElement caller = getCaller();
		final Hasher hasher = Hashing.murmur3_128().newHasher();
		for (final long x : seeds) hasher.putLong(x);
		final long seed = hasher
				.putString(caller.getClassName(), Charsets.UTF_8)
				.putString(caller.getMethodName(), Charsets.UTF_8)
				.hash().asLong();
		return new Random(seed);
	}

	/** Return the first StackTraceElement belonging to a different class. */
	private static StackTraceElement getCaller() {
		final StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();
		for (final StackTraceElement e : stackTrace) {
			if (!e.getClassName().equals(TestHelper.class.getName())) return e;
		}
		throw new RuntimeException("impossible");
	}

	/* Return an array containing both specially selected and random numbers for testing. */
	static int[] testingInts(int count, Random random, boolean allowNegative) {
		checkArgument(count >= 0);
		final ImmutableList<Integer> specials = SPECIAL_INTS;
		final int[] result = new int[specials.size() + count];
		int i=0;
		for (final int x : specials) {
			if (x>=0 || allowNegative) result[i++] = x;
		}
		while (i<result.length) {
			final int x = random.nextInt();
			if (x>=0 || allowNegative) result[i++] = x;
		}
		return result;
	}

	/* Return an array containing both specially selected and random numbers for testing. */
	static long[] testingLongs(int count, Random random, boolean allowNegative) {
		checkArgument(count >= 0);
		final ImmutableList<Long> specials = SPECIAL_LONGS;
		final long[] result = new long[specials.size() + count];
		int i=0;
		for (final long x : specials) {
			if (x>=0 || allowNegative) result[i++] = x;
		}
		while (i<result.length) {
			final long x = random.nextLong();
			if (x>=0 || allowNegative) result[i++] = x;
		}
		return result;
	}

	@SuppressWarnings("boxing") private static ImmutableList<Long> specialLongs() {
		final Set<Long> result = Sets.newHashSet();

		// Add numbers close to powers of two (covers also Long.MAX_VALUE, Integer.MAX_VALUE, etc.).
		for (int i=0; i<64; ++i) {
			for (int j=-1; j<=1; ++j) {
				final long x = (1L << i) + j;
				result.add(x);
			}
		}

		// Add roots of already included numbers and numbers close to them.
		for (final long x : Longs.toArray(result)) {
			if (x<=0) continue;
			final long sqrt = (long) Math.sqrt(x);
			result.add(sqrt);
			result.add(sqrt+1);
		}

		// Add numbers which multiplied with already included numbers produce a value close to Long.MAX_VALUE.
		for (final long x : Longs.toArray(result)) {
			if (x==0) continue;
			final long y = Long.MAX_VALUE / x;
			result.add(y);
			result.add(y+1);
		}

		// Add negatives of already included numbers.
		for (final long x : Longs.toArray(result)) result.add(-x);

		return ImmutableList.copyOf(result);
	}

	@SuppressWarnings("boxing") private static ImmutableList<Integer> specialInts(ImmutableList<Long> specialLongs) {
		final Set<Integer> result = Sets.newHashSet();
		for (final Long x : specialLongs) {
			if (x.intValue() == x.longValue()) result.add(Integer.valueOf(x.intValue()));
		}

		// Add numbers which multiplied with already included numbers produce a value close to Integer.MAX_VALUE.
		for (final int x : Ints.toArray(result)) {
			if (x==0) continue;
			final int y = Integer.MAX_VALUE / x;
			result.add(y);
			result.add(y+1);
		}

		return ImmutableList.copyOf(result);
	}

	private static final ImmutableList<Long> SPECIAL_LONGS = specialLongs();
	private static final ImmutableList<Integer> SPECIAL_INTS = specialInts(SPECIAL_LONGS);
}
