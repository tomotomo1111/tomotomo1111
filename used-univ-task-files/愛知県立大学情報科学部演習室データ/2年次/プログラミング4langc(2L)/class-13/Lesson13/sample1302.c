/* nCrの計算(階乗計算に再帰呼出し利用) */
#include <stdio.h>
#include <stdlib.h>

/* List8-7(p.225): --- 階乗値を返す ---*/
long factorial(long n)
{
       if (n > 0)
               return n * factorial(n - 1);
       else
               return 1;
}

int main(void)
{
    int n, r, ncr;
    
    scanf("%d %d", &n, &r);

    ncr = factorial(n) / (factorial(r) * factorial(n-r));
    
    printf("n=%d, r=%d, nCr=%ld\n", n, r, ncr); // 3つめの %ld を %d に変更した

    return 0;
}
