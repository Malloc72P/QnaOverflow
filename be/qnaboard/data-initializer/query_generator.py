from random_data_supplier import RandomDataSupplier
import random
from progress_bar import ProgressBar


class QueryGenerator:
    # id 생성기
    __current_id = 1
    # query head
    __insert_into_members = "insert into members (members_id, created_date, last_modified_date, deleted, email, nickname, role) values"
    __insert_into_tag = "insert into tag (tag_id, created_date, last_modified_date, author_id, deleted, description, name) values"
    __insert_into_post = "insert into post (post_id, created_date, last_modified_date, author_id, content, deleted, score, title, view_count, post_type, question_id) values"
    __insert_into_question_tag = "insert into question_tag (created_date, last_modified_date, question_id, tag_id) values"
    __insert_into_comment = "insert into comment (comment_id, created_date, last_modified_date, author_id, content, deleted, parent_comment_id, parent_post_id) values"
    __insert_into_vote = "insert into vote (member_id, post_id, vote_type) values"
    # 랜덤값 생성기
    __random_generator = None
    # id 리스트
    __member_ids = []
    __post_ids = []
    __question_ids = []
    __answer_ids = []
    __tag_ids = []

    # 생성자
    def __init__(self):
        self.__random_generator = RandomDataSupplier()

    def create_query(self, member_count, question_count, answer_count, tag_count, comment_count):
        string_list = [self.create_members(member_count), self.create_question(question_count),
                       self.create_answer(answer_count), self.create_tag(tag_count), self.create_question_tag(),
                       self.create_comment(comment_count)]
        return "\n".join(string_list)

    # 멤버 생성 쿼리를 반환함
    def create_members(self, number_of_rows):
        return self.__create_query(number_of_rows, self.__values_members, self.__insert_into_members, 'member')

    # 질문글 생성 쿼리를 반환함
    def create_question(self, number_of_rows):
        return self.__create_query(number_of_rows, self.__values_question, self.__insert_into_post, 'question')

    # 답변글 생성 쿼리를 반환함
    def create_answer(self, number_of_rows):
        return self.__create_query(number_of_rows, self.__values_answer, self.__insert_into_post, 'answer')

    # 태그 생성 쿼리를 반환함
    def create_tag(self, number_of_rows):
        return self.__create_query(number_of_rows, self.__values_tag, self.__insert_into_tag, 'tag')

    # 질문태그 생성 쿼리를 반환함
    def create_question_tag(self):
        values = []
        # 모든 질문글을 순회함
        counter = 1
        for question_id in self.__question_ids:
            ProgressBar.print_progress(counter, len(self.__question_ids), 'Question_Tag', "Complete", 50)
            counter += 1
            tag_ids = set()
            # 태그 5개를 무작위로 선택함(중복허용x)
            for i in range(5):
                tag_ids.add(random.choice(self.__tag_ids))
            for tag_id in tag_ids:
                values.append(self.__values_question_tag(question_id, tag_id))
        joined_values = ",\n".join(values)
        return "{} \n{};".format(self.__insert_into_question_tag, joined_values)

    # 댓글 생성 쿼리를 반환함
    def create_comment(self, number_of_rows):
        return self.__create_query(number_of_rows, self.__values_comment, self.__insert_into_comment, 'comment')

    # 쿼리를 생성해서 반환함
    @staticmethod
    def __create_query(number_of_rows, values_func, query_head, category):
        values = []
        for i in range(number_of_rows):
            ProgressBar.print_progress(i + 1, number_of_rows, category, "Complete", 50)
            value = values_func()
            values.append(value)
        joined_values = ",\n".join(values)
        return "{} \n{};".format(query_head, joined_values)

    # 멤버생성을 위한 values 부분을 하나 만들어서 반환함. 추가로 발급받은 아이디를 리스트에 넣어줌
    def __values_members(self):
        given_id = self.__get_id()
        self.__member_ids.append(given_id)
        given_created_date = self.__random_generator.random_date()
        given_name = self.__random_generator.random_name()
        return "({}, '{}', null, false, 'no-email', '{}', 'USER')".format(given_id, given_created_date, given_name)

    # 질문생성을 위한 values 부분을 하나 만들어서 반환함. 추가로 발급받은 아이디를 리스트에 넣어줌
    def __values_question(self):
        given_id = self.__get_id()
        self.__question_ids.append(given_id)
        self.__post_ids.append(given_id)
        given_created_date = self.__random_generator.random_date()
        given_title = self.__random_generator.random_title()
        given_content = self.__random_generator.random_content()
        given_member_id = random.choice(self.__member_ids)
        return "({}, '{}', null, {}, '{}', false, 0, '{}', 0, 'Question', null)".format(given_id, given_created_date,
                                                                                        given_member_id,
                                                                                        given_content, given_title)

    # 답변생성을 위한 values 부분을 하나 만들어서 반환함. 추가로 발급받은 아이디를 리스트에 넣어줌
    def __values_answer(self):
        given_id = self.__get_id()
        self.__answer_ids.append(given_id)
        self.__post_ids.append(given_id)
        given_created_date = self.__random_generator.random_date()
        given_title = self.__random_generator.random_title()
        given_content = self.__random_generator.random_content()
        given_member_id = random.choice(self.__member_ids)
        given_question_id = random.choice(self.__question_ids)
        return "({}, '{}', null, {}, '{}', false, 0, '{}', 0, 'Answer', {})".format(given_id, given_created_date,
                                                                                         given_member_id, given_content,
                                                                                         given_title, given_question_id)

    # 태그생성을 위한 values 부분을 하나 만들어서 반환함. 추가로 발급받은 아이디를 리스트에 넣어줌
    def __values_tag(self):
        given_id = self.__get_id()
        self.__tag_ids.append(given_id)
        given_created_date = self.__random_generator.random_date()
        given_tag_name = self.__random_generator.random_tag()
        given_tag_desc = self.__random_generator.random_tag_desc()
        given_member_id = random.choice(self.__member_ids)
        return "({}, '{}', null, {}, false, '{}', '{}')".format(given_id, given_created_date, given_member_id,
                                                                given_tag_desc,
                                                                given_tag_name)

    # 질문태그생성을 위한 values 부분을 하나 만들어서 반환함. 추가로 발급받은 아이디를 리스트에 넣어줌
    def __values_question_tag(self, given_question_id, given_tag_id):
        given_created_date = self.__random_generator.random_date()
        return "('{}', null, {}, {})".format(given_created_date, given_question_id, given_tag_id)

    # 댓글생성을 위한 values 부분을 하나 만들어서 반환함. 추가로 발급받은 아이디를 리스트에 넣어줌
    def __values_comment(self):
        given_id = self.__get_id()
        given_created_date = self.__random_generator.random_date()
        given_post_id = random.choice(self.__post_ids)
        given_member_id = random.choice(self.__member_ids)
        given_content = self.__random_generator.random_comment_content()
        return "({}, '{}', null, {}, '{}', false, null, {})".format(given_id, given_created_date, given_member_id,
                                                                    given_content,
                                                                    given_post_id)

    # 아이디를 생성해서 반환함
    def __get_id(self):
        return_value = self.__current_id
        self.__current_id += 1
        return return_value
