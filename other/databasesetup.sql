-- 创建模型表
CREATE TABLE models (
                        model_id INT AUTO_INCREMENT PRIMARY KEY,
                        model_name VARCHAR(255) NOT NULL UNIQUE
);

-- 创建数据表
CREATE TABLE data_info (
                           data_id INT AUTO_INCREMENT PRIMARY KEY,
                           question TEXT NOT NULL,
                           answer TEXT NOT NULL,
                           question_type ENUM('choice', 'judgment', 'short_answer') NOT NULL COMMENT '选择题，判断题，简答题',
                           applicable_scenario VARCHAR(255) NOT NULL,
                           data_source ENUM('input', 'crawler', 'model_generation') NOT NULL COMMENT '手动输入，爬虫，模型生成',
                           model_id INT null,
                           entry_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           is_deleted TINYINT DEFAULT 0 COMMENT '0-未删除，1-已删除',
                           FOREIGN KEY (model_id) REFERENCES models(model_id),
                           CONSTRAINT chk_model_source CHECK (
                               (data_source = 'model_generation' AND model_id IS NOT NULL) OR
                               (data_source <> 'model_generation' AND model_id IS NULL)
                               )
);

-- 创建数据变形表（变形记录表，变形题目成为正式数据）
CREATE TABLE data_transformation (
                                     transformation_id INT AUTO_INCREMENT PRIMARY KEY,
                                     original_data_id INT NOT NULL,
                                     transformed_data_id INT NOT NULL,
                                     transformation_type ENUM('rewrite', 'add_noise', 'reverse_polarity', 'complicate', 'substitute') COMMENT '改写，加噪音，反向极化，复杂化，替代',

                                     FOREIGN KEY (original_data_id) REFERENCES data_info(data_id),
                                     FOREIGN KEY (transformed_data_id) REFERENCES data_info(data_id)
);

--创建环境记录表
CREATE TABLE test_environment (
                                  env_id INT AUTO_INCREMENT PRIMARY KEY,
                                  os_name VARCHAR(50),
                                  cpu_model VARCHAR(100),
                                  gpu_info VARCHAR(100)
);

-- 创建测试相关信息表
CREATE TABLE test_info (
                           test_id INT AUTO_INCREMENT PRIMARY KEY,
                           test_name VARCHAR(255) NOT NULL,
                           test_method TEXT NOT NULL,
                           env_id INT NOT NULL,
                           model_id INT NOT NULL,
                           test_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           test_status ENUM('pending', 'approved', 'rejected') DEFAULT 'pending' COMMENT '待处理，通过，拒绝',
                           FOREIGN KEY (model_id) REFERENCES models(model_id),
                           FOREIGN KEY (env_id) REFERENCES test_environment(env_id)
);

-- 创建测试与题目关联表
CREATE TABLE test_question_relations (
                                         id INT AUTO_INCREMENT PRIMARY KEY,
                                         test_id INT,
                                         data_id INT,
                                         FOREIGN KEY (test_id) REFERENCES test_info(test_id) ON DELETE CASCADE,
                                         FOREIGN KEY (data_id) REFERENCES data_info(data_id) ON DELETE CASCADE
);

-- 创建测试结果表
CREATE TABLE test_results (
                              result_id INT AUTO_INCREMENT PRIMARY KEY,
                              test_id INT,
                              data_id INT,

                              model_output TEXT,
                              is_correct BOOLEAN,
                              response_time INT COMMENT '响应时间(毫秒)',

                              FOREIGN KEY (test_id) REFERENCES test_info(test_id),
                              FOREIGN KEY (data_id) REFERENCES data_info(data_id)
);
