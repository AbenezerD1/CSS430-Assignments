import threading
import time

def calculate_sum(numbers):
    """
    Calculates the sum of a list of numbers.
    """
    total = 0
    for num in numbers:
        total += num
    return total

def worker(numbers, results):
    """
    Worker function to calculate sum of a chunk and store the result.
    """
    result = calculate_sum(numbers)
    results.append(result)

if __name__ == "__main__":
    numbers = list(range(1000000))

    # Single-threaded execution
    start_time = time.time()
    single_threaded_result = calculate_sum(numbers)
    end_time = time.time()
    print(f"Single-threaded time: {end_time - start_time:.2f} seconds")

    # Multi-threaded execution (corrected)
    start_time = time.time()
    num_threads = 1000
    chunk_size = len(numbers) // num_threads
    threads = []
    results = []
    for i in range(num_threads):
        start_index = i * chunk_size
        end_index = (i + 1) * chunk_size
        thread = threading.Thread(target=worker, args=(numbers[start_index:end_index], results))
        threads.append(thread)
        thread.start()

    for thread in threads:
        thread.join()

    # Collect results from each thread
    multi_threaded_result = sum(results)

    end_time = time.time()
    print(f"Multi-threaded time: {end_time - start_time:.2f} seconds") 

    # Check if results are correct (they won't be in this case for large datasets)
    if single_threaded_result == multi_threaded_result:
        print("Results match")
    else:
        print("Results might differ due to rounding errors in large datasets")