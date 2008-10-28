import java.util.Random;

public class MyRandom extends Random {
	public double nextGaussian(double mean, double std_dev) {
		double norm;
		norm = std_dev * nextGaussian() + mean;
		return norm;
	}

	public double nextUniform(double lower, double upper) {
		return (upper-lower) * nextDouble() - (upper-lower)/2;
	}
}

