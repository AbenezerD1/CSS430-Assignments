import threading
import time

def worker(num):
    """
    Simulates a time-consuming task.
    """
    print(f"Worker {num} starting")
    time.sleep(2)  # Simulate a long-running task
    print(f"Worker {num} finished")

if __name__ == "__main__":
    start_time = time.time()

    # Single-threaded execution
    for i in range(5):
        worker(i)

    end_time = time.time()
    print(f"Single-threaded time: {end_time - start_time:.2f} seconds")

    start_time = time.time()

    # Multi-threaded execution
    threads = []
    for i in range(5):
        thread = threading.Thread(target=worker, args=(i,))
        threads.append(thread)
        thread.start()

    for thread in threads:
        thread.join()

    end_time = time.time()
    print(f"Multi-threaded time: {end_time - start_time:.2f} seconds")