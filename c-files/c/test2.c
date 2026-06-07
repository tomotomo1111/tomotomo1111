/* CやC++などシェルに実行結果コード返却を明示する言語を利用する場合 基本的に0を返却してください。 */
#include <stdio.h>

int main()
{
    int n, m, r, x1, x2, t1, t2;
    scanf("%d %d %d", &n, &m ,&r);
    scanf("%d %d", &x1, &t1);
    scanf("%d %d", &x2, &t2);
    long car_time = bycar(n, x2, t2, m, r);
    long onfoot_time = onfoot(n, x1, t1);
    printf("%ld\n", (car_time < onfoot_time) ? car_time : onfoot_time );
    return 0;
}

long int onfoot(int n, int x1, int t1) {
    int count = n / x1;
    if (n % x1 != 0) count++;
    return (long) (count * t1 * 2);
}

long int bycar(int n, int x2, int t2, int m, int r) {
    int count = n / x2;
    if (n % x2 != 0) count++;
    int gas = count / m;
    return (long) (count * t2 * 2 + gas * r);
}