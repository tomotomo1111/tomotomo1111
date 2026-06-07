/*
差分近似法プログラム
*/
#include <stdio.h>

/*
  前進差分近似関数
  [入力]
    a: 前進差分を求める点
    h: 分割の幅
    f: 関数ポインタ
  [返却値]
    前進差分 (f(a+h)-f(a))/h の値
*/
double fwdf(double a, double h, double (* f)(double))
{
  return(((* f)(a+h)-(* f)(a))/h);
}

/*
  後退差分近似関数
  [入力]
    a: 後退差分を求める点
    h: 分割の幅
    f: 関数ポインタ
  [返却値]
    前進差分 (f(a)-f(a-h))/h の値
*/
double bkdf(double a, double h, double (* f)(double))
{
    /* [演習] 講義スライドp.3を参考にして後退差分近似のプログラムを作成してください．*/
}

/*
  中心差分近似関数
  [入力]
    a: 中心差分を求める点
    h: 分割の幅
    f: 関数ポインタ
  [返却値]
    中心差分 (f(a+h)-f(a-h))/(2h) の値
*/
double ctdf(double a, double h, double (* f)(double))
{
    /* [演習] 講義スライドp.3を参考にして中心差分近似のプログラムを作成してください．*/
}
