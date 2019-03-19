import glob

f = open("data.txt", "w")
for filename in glob.glob('log*.txt'):

    file = open(filename,'r')
    total=0
    ts=0
    tj=0

    for i in file.readlines():
        list_ts_tj=i.split()
        tj+=int(list_ts_tj[0])
        ts += int(list_ts_tj[1])
        total+=1

    avg_ts=ts/total/1000000
    avg_tj=tj/total/1000000

    f.write('{}      {:.2f}   {:.2f}\n'.format(filename, avg_ts,avg_tj))
f.close()
