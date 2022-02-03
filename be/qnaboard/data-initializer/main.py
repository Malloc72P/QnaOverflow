from query_generator import QueryGenerator


class DataInitializer:
    __member_count = 124
    __question_count = 368790
    __answer_count = 478790
    __tag_count = 42
    __comment_count = 578790
    # data.sql파일
    __file_data_sql = None
    # 쿼리 생성기
    __query_generator = None

    def __init__(self):
        self.__query_generator = QueryGenerator()
        self.__file_data_sql = open("../src/main/resources/data.sql", "w")

    def initialize(self):
        query = self.__query_generator.create_query(self.__member_count, self.__question_count, self.__answer_count, self.__tag_count, self.__comment_count)
        self.__write_string(query)

    def __write_string(self, string):
        self.__file_data_sql.write(string + "\n")


# 데이터 이니셜라이져 객체 생성 후 데이터 초기화
initializer = DataInitializer()
initializer.initialize()
