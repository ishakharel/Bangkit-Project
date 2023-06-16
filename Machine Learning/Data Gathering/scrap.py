from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
from webdriver_manager.chrome import ChromeDriverManager
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import requests
import io
from PIL import Image
import time

# Webscrapping Using Selenium
# Specify the path of chromedriver.exe
PATH = "C:\\Users\\ACER\\OneDrive\\Documents\\Project\\Web scrapping\\chromedriver.exe"

# Initial setup for webdriver
options = webdriver.ChromeOptions()
options.add_experimental_option("detach", True)
wd = webdriver.Chrome(service=Service(ChromeDriverManager().install()),options=options)
wd.maximize_window()

# Function for getting the image source from web
def get_images_from_google(wd, delay, max_images):
    def scroll_down(wd):
        wd.execute_script("window.scrollTo(0, document.body.scrollHeight);")
        time.sleep(delay)

    # Specify the web url to scrappe image from
    url = "https://www.google.com/search?q=babi&rlz=1C1CHBF_enID1035ID1035&source=lnms&tbm=isch&sa=X&ved=2ahUKEwiFoqTSyZP_AhX_8TgGHQR5DAsQ_AUoAXoECAMQAw&biw=1536&bih=746&dpr=1.25"
    wd.get(url)

    image_urls = set()
    skips = 0
    counter = 0

    while len(image_urls)<max_images:
        scroll_down(wd)
        thumbnails = wd.find_elements(By.CLASS_NAME,"Q4LuWd")
        
        for img in thumbnails[len(image_urls)+skips:]:
            if counter == max_images:
                break
            try:
                WebDriverWait(wd, 20).until(EC.element_to_be_clickable(img)).click()
                time.sleep(delay)

            except Exception as e:
                print("Failed to get the image source")
                continue
                

            images = wd.find_elements(By.CLASS_NAME, "iPVvYb")
            for image in images:
                
                if image.get_attribute('src') in image_urls:
                    skips +=1
                    break

                if image.get_attribute('src') and 'http' in image.get_attribute('src'):
                    counter +=1
                    image_urls.add(image.get_attribute('src'))
                    print(f"Found {len(image_urls)} image")
    return image_urls

# Function to download the image to local storage
def download_image(download_path, url, file_name ):
    try:
        image = requests.get(url, timeout=10)
        image.raise_for_status() 
        image_content = image.content

        image_file = io.BytesIO(image_content)
        image = Image.open(image_file)
        file_path = download_path +file_name

        with open(file_path, "wb") as f:
            image.save(f, "JPEG")
        print("Success")
    except Exception as e:
        print('Failed -', e)

urls = get_images_from_google(wd,1,20)

for i, url in enumerate(urls):
    download_image('FilePath/', url, "FileName ("+str(i+1)+").jpg")

print(urls)
wd.quit()