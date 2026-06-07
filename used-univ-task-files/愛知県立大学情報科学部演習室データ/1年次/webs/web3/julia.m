function julia(c)
  h=zeros(400,400);
  for p=1:400
    x=(p-200)/200;
    for q=1:400
      y=(q-200)/200;

      h(q,p)=k;
    end
  end
  h = (h-min(h,[],'all'))*256/(max(h,[],'all')-min(h,[],'all')+1);
  imshow(h,cmap);
end

