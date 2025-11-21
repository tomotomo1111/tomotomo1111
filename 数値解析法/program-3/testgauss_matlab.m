n = 100;
A = gallery('minij',n);
exactx = 1.1*ones(n,1);
b = A*exactx;
x = linsolve(A,b);
err = norm(exactx - x) / norm(exactx);
fprintf("相対誤差 = %e\n", err);