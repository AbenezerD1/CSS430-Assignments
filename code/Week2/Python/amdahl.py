import matplotlib.pyplot as plt
import numpy as np

def amdahl_law(f_serial, num_processors):
    """
    Calculates the speedup according to Amdahl's Law.

    Args:
        f_serial: The fraction of the program that is inherently serial (0 to 1).
        num_processors: The number of processors used.

    Returns:
        The speedup achieved.
    """
    return 1 / (f_serial + (1 - f_serial) / num_processors)

def plot_amdahl(f_serial_values, max_processors=100):
    """
    Plots Amdahl's Law speedup for different serial fractions.

    Args:
        f_serial_values: A list of serial fraction values.
        max_processors: The maximum number of processors to consider for the plot.
    """
    processors = np.arange(1, max_processors + 1)
    
    plt.figure(figsize=(10, 6))  # Adjust figure size for better visualization

    for f_serial in f_serial_values:
        speedup = [amdahl_law(f_serial, p) for p in processors]
        plt.plot(processors, speedup, label=f"Serial Fraction = {f_serial}")

    plt.xlabel("Number of Processors")
    plt.ylabel("Speedup")
    plt.title("Amdahl's Law: Speedup vs. Number of Processors")
    plt.grid(True)
    plt.legend()
    plt.xlim(1, max_processors) #Set x limits to start at 1
    plt.ylim(1, max_processors) #Set y limits to start at 1 and roughly be the same as x. This makes it easy to see diminishing returns.
    plt.show()

# Example usage:
f_serial_values = [0.01, 0.1, 0.2, 0.5]  # Different serial fractions
plot_amdahl(f_serial_values, max_processors = 50) #Plot with up to 50 processors

#Demonstrate the impact of a very small serial fraction.
f_serial_values = [0.001]
plot_amdahl(f_serial_values, max_processors = 500) #Plot with up to 500 processors

#Show the theoretical max speedup for certain serial fractions.
print(f"Theoretical max speedup for 1% serial portion: {amdahl_law(0.01, float('inf'))}")
print(f"Theoretical max speedup for 10% serial portion: {amdahl_law(0.1, float('inf'))}")
print(f"Theoretical max speedup for 20% serial portion: {amdahl_law(0.2, float('inf'))}")
print(f"Theoretical max speedup for 50% serial portion: {amdahl_law(0.5, float('inf'))}")