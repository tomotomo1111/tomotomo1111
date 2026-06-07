clearvars;
close all;
% PDE toolbox 使用例（ヘルムホルツ方程式）
omega = 4*pi;
square = [3; 4; -5; -5; 5; 5; -5; 5; 5; -5];
diamond = [2; 4; 2.1; 2.4; 2.7; 2.4; 1.5; 1.8; 1.5; 1.2];
gd = [square,diamond];
ns = char('square','diamond')';
sf = 'square - diamond';
g = decsg(gd,sf,ns);

figure; 
pdegplot(g,EdgeLabels="on"); 
xlim([-6,6]);
ylim([-6,6]);
saveas(gcf,'pde_shape.png');

emagmodel = femodel(AnalysisType="electricHarmonic",Geometry=g);

emagmodel.VacuumPermittivity = 1;
emagmodel.VacuumPermeability = 1;

emagmodel.MaterialProperties = ...
    materialProperties(RelativePermittivity=1, ...
                       RelativePermeability=1, ...
                       ElectricalConductivity=0);

ffbc = farFieldBC(Thickness=2,Exponent=4,Scaling=1);
emagmodel.EdgeBC(1:4) = edgeBC(FarField=ffbc);

innerBCFunc = @(location,~) [-exp(-1i*omega*location.x); ...
                            zeros(1,length(location.x))];
emagmodel.EdgeBC(5:8) = edgeBC(ElectricField=innerBCFunc);

emagmodel = generateMesh(emagmodel,Hmax=0.1);
figure;
pdemesh(emagmodel); 
axis equal;
saveas(gcf,'pde_mesh.png');

result = solve(emagmodel,omega);

u = result.ElectricField;
figure
pdeplot(result.Mesh, ...
        XYData=real(u.Ex), ...
        Mesh="off");
colormap(jet)
saveas(gcf,'pde_result.png');