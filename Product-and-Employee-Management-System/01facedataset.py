import cv2
import sys

cam = cv2.VideoCapture(0)
cam.set(3, 640) # set video width
cam.set(4, 480) # set video height

face_detector = cv2.CascadeClassifier('haarcascade_frontalface_default.xml')

# For each person, enter one numeric face id
face_id = sys.argv[1]
my_id = [1,2]

# Mở file để ghi
with open("myidlist.txt", "w") as f:
    for item in my_id:
        # Ghi từng phần tử của list xuống mỗi dòng của file
        f.write("%s\n" % item)
with open("mynamelist.txt", "r") as f:
    # Đọc từng dòng của file và lưu vào list
    names = [line.strip() for line in f.readlines()]
face_name = sys.argv[2]
isExitName = 0
for item in names:
    if item == face_name:
        isExitName = 1
if isExitName == 0:
    names.append(face_name)

# Mở file để ghi
with open("mynamelist.txt", "w") as f:
    for item in names:
        # Ghi từng phần tử của list xuống mỗi dòng của file
        f.write("%s\n" % item)

print("\n [INFO] Initializing face capture. Look the camera and wait ...")
# Initialize individual sampling face count
count = 0

while(True):

    ret, img = cam.read()
    img = cv2.flip(img, 1) # flip video image vertically
    gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    faces = face_detector.detectMultiScale(gray, 1.3, 5)

    for (x,y,w,h) in faces:

        cv2.rectangle(img, (x,y), (x+w,y+h), (255,0,0), 2)     
        count += 1

        # Save the captured image into the datasets folder
        cv2.imwrite("dataset/User." + str(face_id) + '.' + str(count) + ".jpg", gray[y:y+h,x:x+w])

        cv2.imshow('image', img)

    k = cv2.waitKey(1000) & 0xff # Press 'ESC' for exiting video
    if k == 27:
        break
    elif count >= 10: # Take 30 face sample and stop video
         break

# Do a bit of cleanup
print("\n [INFO] Exiting Program and cleanup stuff")
cam.release()
cv2.destroyAllWindows()


