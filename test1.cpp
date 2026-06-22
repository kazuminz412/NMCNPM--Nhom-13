#include <iostream>

using namespace std;

const int MOD = 1e9 + 7;

void solveDP(int n)
{
    // Tối ưu 1: Thay vì dùng mảng a[N], b[N], c[N] tốn O(N) bộ nhớ,
    // ta chỉ cần lưu các trạng thái ngay trước nó (O(1) bộ nhớ).
    long long a_prev = 1, b_prev = 1, c_prev = 2; // n = 0
    long long c_prev_prev = 0;                    // Tương đương C[-1] = 0

    if (n == 0)
    {
        cout << a_prev << " " << b_prev << " " << c_prev << "\n";
        return;
    }

    long long a_curr, b_curr, c_curr;

    for (int i = 1; i <= n; ++i)
    {
        // Tối ưu 2: Ép kiểu hoặc khai báo sẵn long long để tránh tràn số
        // TRƯỚC khi thực hiện phép chia lấy dư (Modulo).
        // Phải tính C trước vì B phụ thuộc vào C_curr.
        c_curr = (a_prev + b_prev + c_prev) % MOD;

        b_curr = (a_prev + c_curr) % MOD;

        a_curr = (b_prev + c_prev_prev) % MOD;

        // Cập nhật lại các biến trượt cho vòng lặp tiếp theo
        c_prev_prev = c_prev;

        a_prev = a_curr;
        b_prev = b_curr;
        c_prev = c_curr;
    }

    cout << a_curr << " " << b_curr << " " << c_curr << "\n";
}

int main()
{
    // Tối ưu 3: Fast I/O giúp vượt qua các testcase có input/output khổng lồ
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);

    int n;
    if (cin >> n)
    {
        solveDP(n);
    }
    return 0;
}