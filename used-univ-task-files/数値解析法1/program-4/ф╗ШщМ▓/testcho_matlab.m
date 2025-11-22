n = 100;
A = gallery('minij',n);
exactx = 1.1*ones(n,1);
b = A*exactx;
S =chol(A);
y = S'\b;
x = S\y;
err = norm(exactx - x) / norm(exactx);
fprintf("相対誤差（コレスキー法） = %e\n", err);

% \ を使用すると方法が自動選択される（この例ではコレスキー法を選択）
xauto = A \ b;
errauto = norm(exactx - xauto) / norm(exactx);
fprintf("相対誤差（自動選択） = %e\n", errauto);