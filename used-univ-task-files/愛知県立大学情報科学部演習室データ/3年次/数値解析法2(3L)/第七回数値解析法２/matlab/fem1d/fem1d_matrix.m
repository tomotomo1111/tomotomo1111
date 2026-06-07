function matA = fem1d_matrix(xi,pfunc,qfunc)
    % xi: 節点x_i. MATLABのインデックスは1から始まるため，スライドのインデックスに+1すること
    % pfunc: 関数p(x)
    % qfunc: 関数q(x)
    [~,n] = size(xi);
    n = n-2; %行列の次元数は節点数-2
    a = zeros(n,1); % Aの対角成分
    bl = zeros(n,1); % Aの対角成分の下部分
    bu = zeros(n,1); % Aの対角成分の上部分
    for i = 1:n-1
        hm = xi(i+1)-xi(i); %スライドのh_i 
        hp = xi(i+2)-xi(i+1); %スライドのh_{i+1}
        % [演習] 対角成分をa(i)へ，下部分をbl(i)へ格納するプログラムを作成しないさい．
        %       （fem1d_rvec.mが参考になります）
        bu(i+1) = bl(i);
    end
    hm = xi(n+1)-xi(n);
    hp = xi(n+2)-xi(n+1);
    % [演習] 対角成分をa(n)へ格納するプログラムを作成しないさい．
    matA = spdiags([bl a bu],-1:1,n,n);
end