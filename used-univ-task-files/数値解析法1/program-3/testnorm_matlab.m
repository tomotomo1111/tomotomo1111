clear;
m=100;n=100;
xg=1:n; %行ベクトルになる
x = xg'; %転置すると列ベクトル
A = gallery('minij',n);
x1 = norm(x,1);
x2 = norm(x);
xinf = norm(x,Inf);
fprintf("ベクトルノルム: 1ノルム = %e, 2ノルム = %e, 最大値ノルム = %e\n",x1,x2,xinf);
A1 = norm(A,1);
A2 = norm(A);
Ainf = norm(A,Inf);
Afro = norm(A,'fro');
fprintf("行列ノルム: 1ノルム = %e, 2ノルム = %e, 最大値ノルム = %e，フロベニウスノルム = %e\n",A1,A2,Ainf,Afro);