function mapimage(param,id)

lw=1;
load colorv
load mat4im

input=[id' param'/(max(param))];

tmp2=isnan(input(:,2));
input(tmp2,:)=[];

figure(2);
mapshow(rlon_mat,rlat_mat,rJ);hold on
S=shaperead('Export_Output');
for z=1:size(S,1)
    z
    mapshow(S(z),'facecolor','none','edgecolor','k',...
        'linewidth',lw);hold on;
end
hold on;
paint_shape(input,S,lw,c);
set(gca,'fontsize',10);colormap(jet);colorbar;
xlabel('Longitude');ylabel('Latitude');
hold off;
