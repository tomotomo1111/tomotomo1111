function statreducer(~,intermValIter,outKVStore)

tmpfare=[];tmpnum=[];
c=0;
while hasnext(intermValIter) % Check if results from map function still exist
    tmp=getnext(intermValIter); % Get results from map function
    tmp2=tmp{:}; % Cell array to double array
    c=c+1;
    tmpfare(c,:)=tmp2(:,1); 
    tmpnum(c,:)=tmp2(:,2);
end

stat.fare=sum(tmpfare,1)./sum(tmpnum,1); % Calculate averaged fare of each zone
stat.num=sum(tmpnum,1); % Calculate total number of each zone

add(outKVStore,'count',stat); % Save output
end