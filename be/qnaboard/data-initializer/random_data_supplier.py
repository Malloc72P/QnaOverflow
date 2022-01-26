import datetime
import random
import csv


class RandomDataSupplier:
    # 랜덤날짜의 범위(시작, 종료일)
    __start_date = datetime.date(2020, 1, 1)
    __end_date = datetime.date(2020, 2, 1)
    # 랜덤 값이 저장되어 있는 리스트
    __random_names = []
    __random_tags = []
    __random_short_sentences = []
    __random_long_sentences = []

    def __init__(self):
        self.__prepare_files()

    # 필요한 파일을 생성하거나 불러옴
    def __prepare_files(self):
        self.__random_names = self.__file_to_list("csv/names.csv")
        self.__random_tags = self.__file_to_list("csv/tags.csv")
        self.__random_short_sentences = self.__file_to_list("csv/short_sentences.csv")
        self.__random_long_sentences = self.__file_to_list("csv/long_sentences.csv")

    # 랜덤날짜를 생성해서 반환함
    def random_date(self):
        time_between_dates = self.__end_date - self.__start_date
        days_between_dates = time_between_dates.days
        random_number_of_days = random.randrange(days_between_dates)
        random_date = self.__start_date + datetime.timedelta(random_number_of_days)
        return random_date

    # 랜덤이름을 생성해서 반환함
    def random_name(self):
        random_name = random.choice(self.__random_names)
        return self.__without_bracket(random_name)

    # 랜덤태그를 생성해서 반환함
    def random_tag(self):
        random_tag = random.choice(self.__random_tags)
        return self.__without_bracket(random_tag)

    # 랜덤태그 설명을 생성해서 반환함
    def random_tag_desc(self):
        random_tag_desc = random.choice(self.__random_short_sentences)
        return self.__without_bracket(random_tag_desc)

    # 랜덤제목을 생성해서 반환함
    def random_title(self):
        random_title = random.choice(self.__random_short_sentences)
        return self.__without_bracket(random_title)

    # 랜덤댓글내용을 생성해서 반환함
    def random_comment_content(self):
        random_comment = random.choice(self.__random_short_sentences)
        return self.__without_bracket(random_comment)

    # 랜덤컨텐츠를 생성해서 반환함
    def random_content(self):
        random_title = random.choice(self.__random_long_sentences)
        return self.__without_bracket(random_title)

    # 파일명을 파라미터로 받아서 파일을 열고 csv reader를 만들어서 라인단위로 읽은 다음 리스트에 담아서 반환함
    @staticmethod
    def __file_to_list(file_name):
        file_object = open(file_name, "r")
        csv_reader = csv.reader(file_object, delimiter="\t")
        random_list = []
        for i, line in enumerate(csv_reader):
            random_list.append(line)
        return random_list

    # ['']없이 문자열만 꺼내옴
    @staticmethod
    def __without_bracket(string):
        return str(string)[2:-2]
