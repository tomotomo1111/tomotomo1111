clear;
[r,n]=meshgrid(0:0.1:1,1:16);
Sn=n./((n-1).*r+1)
surf(r,n,Sn);colorbar;
xlabel('r'),ylabel('n'),zlabel('Sn')
print('wk1-1.pdf','-dpdf');
hold off;