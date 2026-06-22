#include <iostream>
using namespace std;

const int mod = 1e9 + 7;
long long a[5000];
long long b[5000];
long long c[5000];

void again(int n)
{
    a[1] = 1;
    b[1] = 1;
    c[0] = 0;
    c[1] = 2;
    for (int i = 2; i < n + 2; i++)
    {
        c[i] = (a[i - 1] + b[i - 1] + c[i - 1]) % mod;
        b[i] = (a[i - 1] + c[i]) % mod;
        a[i] = (b[i - 1] + c[i - 2]) % mod;
    }
    cout << a[n + 1] << " " << b[n + 1] << " " << c[n + 1] << "\n";
}

int main()
{
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);
    int n;
    cin >> n;
    again(n);
    return 0;
}