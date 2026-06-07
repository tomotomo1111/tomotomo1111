function [gdx,gdy,ufdm] = fdm2d_Poisson(xitv,yitv,nx,ny,srcf,bdg)
    % 分割幅の計算および格子作成
    hx = (xitv(2)-xitv(1))/(nx+1);
    hy = (yitv(2)-yitv(1))/(ny+1);
    x = xitv(1):hx:xitv(2);
    y = yitv(1):hy:yitv(2);
    [gdx,gdy] = meshgrid(x,y);

    % 解行列の初期化と境界値埋め込み
    ufdm = zeros(nx+2,ny+2);
    for i = 1:nx+2
        ufdm(i,1)=bdg(gdx(i),gdy(1));
        ufdm(i,ny+2)=bdg(gdx(i),gdy(ny+2));
    end
    for j = 2:ny+1
        ufdm(1,j)=bdg(gdx(1),gdy(j));
        ufdm(nx+2,j)=bdg(gdx(nx+2),gdy(j));
    end

    % 
    % [演習] 差分行列の作成コードを入力してください．なお差分行列は matA へ格納すること．
    
    % 右辺ベクトルの作成
    matf = zeros(nx,ny);
    matf(1,1)=srcf(x(2),y(2))+bdg(x(1),y(2))/hx^2+bdg(x(2),y(1))/hy^2;
    matf(1,ny)=srcf(x(2),y(ny+1))+bdg(x(1),y(ny+1))/hx^2+bdg(x(2),y(ny+2))/hy^2;
    matf(nx,1)=srcf(x(nx+1),y(2))+bdg(x(nx+2),y(2))/hx^2+bdg(x(nx+1),y(1))/hy^2;
    matf(nx,ny)=srcf(x(nx+1),y(ny+1))+bdg(x(nx+2),y(ny+1))/hx^2+bdg(x(nx+1),y(ny+2))/hy^2;
    for j=2:ny-1
        matf(1,j)=srcf(x(2),y(j+1))+bdg(x(1),y(j+1))/hx^2;
        matf(nx,j)=srcf(x(nx+1),y(j+1))+bdg(x(nx+2),y(j+1))/hx^2;
    end
    for i=2:nx-1
        matf(i,1)=srcf(x(i+1),y(2))+bdg(x(i+1),y(1))/hy^2;
        matf(i,ny)=srcf(x(i+1),y(ny+1))+bdg(x(i+1),y(ny+2))/hy^2;
    end
    for i=2:nx-1
        for j=2:ny-1
            matf(i,j)=srcf(x(i+1),y(j+1));
        end
    end
    vecf = reshape(matf,[nx*ny,1]);

    % 解ベクトルの計算と返却配列への格納
    uvec = matA\vecf;
    ufdm = [ufdm(1,1:nx+2);ufdm(2:ny+1,1) reshape(uvec,[nx,ny])' ufdm(2:ny+1,nx+2);ufdm(ny+2,1:nx+2)];
end