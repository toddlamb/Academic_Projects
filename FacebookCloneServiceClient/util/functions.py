import random
import string

def get_random_str(N):
    return ''.join(random.choices(string.ascii_letters, k=N))
