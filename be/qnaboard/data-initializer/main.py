# 데이터 초기화를 위한 스크립트

# Press Shift+F10 to execute it or replace it with your code.
# Press Double Shift to search everywhere for classes, files, tool windows, actions, and settings.
import datetime
import random
import csv


class DataInitializer:
    # data.sql파일
    __file_data_sql = None
    # 랜덤날짜의 범위(시작, 종료일)
    __start_date = datetime.date(2020, 1, 1)
    __end_date = datetime.date(2020, 2, 1)
    # 랜덤 값이 저장되어 있는 리스트
    __random_names = []
    __random_sentences = []

    # 초기화 함수
    def initialize(self):
        self.__prepare_files()

    # 필요한 파일을 생성하거나 불러옴
    def __prepare_files(self):
        self.__file_data_sql = open("data.sql", "wb")
        self.__random_names = self.__file_to_list("names.csv")
        self.__random_sentences = self.__file_to_list("sentences.csv")

    # 파일명을 파라미터로 받아서 파일을 열고 csv reader를 만들어서 라인단위로 읽은 다음 리스트에 담아서 반환함
    @staticmethod
    def __file_to_list(file_name):
        file_object = open(file_name, "r")
        csv_reader = csv.reader(file_object, delimiter="\t")
        random_list = []
        for i, line in enumerate(csv_reader):
            random_list.append(line)
        return random_list

    # 랜덤날짜를 생성해서 반환함
    def __random_date(self):
        time_between_dates = self.__end_date - self.__start_date
        days_between_dates = time_between_dates.days
        random_number_of_days = random.randrange(days_between_dates)
        random_date = self.__start_date + datetime.timedelta(random_number_of_days)
        return random_date

    # 랜덤이름을 생성해서 반환함
    def __random_name(self):
        random_name = random.choice(self.__random_names)
        return self.__without_bracket(random_name)

    # 랜덤제목을 생성해서 반환함
    def __random_title(self):
        random_title = random.choice(self.__random_sentences)
        return self.__without_bracket(random_title)

    # 랜덤컨텐츠를 생성해서 반환함
    def __random_content(self):
        content = ""
        for i in range(10):
            random_sentence = random.choice(self.__random_sentences)
            content += self.__without_bracket(random_sentence) + " "
        return content

    @staticmethod
    def __without_bracket(string):
        return str(string)[2:-2]


# 데이터 이니셜라이져 객체 생성 후 데이터 초기화
initializer = DataInitializer()
initializer.initialize()
