function mandel
  h=zeros(400,400);
  for p=1:400
    a=(p-200)/100;
    for q=1:400
      b=(q-200)/100;
      c=a+b*i;
      z=0+0*i;
      for k=1:30
        z=z^2+c;
        if abs(z)>2
          break
        end
      end
      h(q,p)=k;
    end
  end
  h = (h-min(h,[],'all'))*256/(max(h,[],'all')-min(h,[],'all')+1);
  imshow(h,cmap);
end
