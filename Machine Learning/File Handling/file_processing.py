import os

def count_file(dir):
    list_dir = os.listdir(dir)
    count = 0
    for item in list_dir:
        fullPath = os.path.join(dir,item)
        if os.path.isdir(fullPath):
            count +=1
            count += count_file(fullPath)
            return count
        else:
            count += 1
    
    for (root,dirs,files) in os.walk(dir):
        print (root)
        print (dirs)
        print (files)
        print ('--------------------------------')
    return count

def rename_file(dir, count=0):
    list_dir = os.listdir(dir)
    print(list_dir)
    for item in list_dir:
        fullPath = os.path.join(dir,item)
        if os.path.isdir(fullPath):
            rename_file(fullPath,count)
        else:
            count += 1
            newName = "medications ("+ str(count+405) +").jpg"
            os.rename(fullPath,os.path.join(dir,newName))
    
    # for (root,dirs,files) in os.walk(dir):
    #     print (root)
    #     print (dirs)
    #     print (files)
    #     print ('--------------------------------')
    return count

# print(count_file("dir"))
print(rename_file("C:\\Users\\ACER\\OneDrive\\Documents\\Project\\Web scrapping\\medications\\Qualified"))

#plastic_bag (403)