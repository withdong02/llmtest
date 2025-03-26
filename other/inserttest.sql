-- 向 models 表插入数据
INSERT INTO models (model_name) VALUES
                                    ('ModelA'),
                                    ('ModelB');

-- 向 data_info 表插入数据
INSERT INTO data_info (question, answer, question_type, applicable_scenario, data_source, model_id) VALUES
                                                                                                        ('1 + 1 等于多少？', '2', 'short_answer', '基础数学测试', 'input', NULL),
                                                                                                        ('一加一等于几？', '2', 'short_answer', '基础数学测试', 'input', NULL),
                                                                                                        ('2 - 0 等于多少？', '2', 'short_answer', '基础数学测试', 'input', NULL),
                                                                                                        ('地球是太阳系的第几颗行星？', '第三颗', 'short_answer', '天文知识测试', 'crawler', NULL),
                                                                                                        ('太阳系中从内往外数地球排第几？', '第三颗', 'short_answer', '天文知识测试', 'crawler', NULL),
                                                                                                        ('太阳系里除了地球，还有几颗行星？', '七颗', 'short_answer', '天文知识测试', 'crawler', NULL),
                                                                                                        ('该模型预测结果是否准确？', '是', 'judgment', '模型评估测试', 'model_generation', 1),
                                                                                                        ('这个模型预测的结果准不准呀？', '是', 'judgment', '模型评估测试', 'model_generation', 1),
                                                                                                        ('该模型预测结果不准确吗？', '否', 'judgment', '模型评估测试', 'model_generation', 1),
                                                                                                        ('三角形内角和是多少度？', '180 度', 'short_answer', '几何知识测试', 'input', NULL),
                                                                                                        ('三角形三个内角相加是多少度？', '180 度', 'short_answer', '几何知识测试', 'input', NULL),
                                                                                                        ('等边三角形每个内角是多少度？', '60 度', 'short_answer', '几何知识测试', 'input', NULL),
                                                                                                        ('以下哪个是哺乳动物：A. 青蛙 B. 蝙蝠 C. 金鱼', 'B', 'choice', '生物知识测试', 'crawler', NULL),
                                                                                                        ('下列动物中属于哺乳动物的是：A. 猫 B. 鸟 C. 鱼', 'A', 'choice', '生物知识测试', 'crawler', NULL),
                                                                                                        ('以下哪种动物是胎生的：A. 蛇 B. 狗 C. 鸡', 'B', 'choice', '生物知识测试', 'crawler', NULL),
                                                                                                        ('水在标准大气压下的沸点是多少摄氏度？', '100', 'short_answer', '物理知识测试', 'model_generation', 2),
                                                                                                        ('标准大气压下，水加热到多少度会沸腾？', '100', 'short_answer', '物理知识测试', 'model_generation', 2),
                                                                                                        ('在标准大气压下，水的冰点是多少摄氏度？', '0', 'short_answer', '物理知识测试', 'model_generation', 2);

-- 向 data_transformation 表插入数据
INSERT INTO data_transformation (original_data_id, transformed_data_id, transformation_type) VALUES
                                                                                                 (1, 2, 'rewrite'),
                                                                                                 (1, 3, 'add_noise'),
                                                                                                 (4, 5, 'rewrite'),
                                                                                                 (4, 6, 'complicate'),
                                                                                                 (7, 8, 'rewrite'),
                                                                                                 (7, 9, 'reverse_polarity'),
                                                                                                 (10, 11, 'rewrite'),
                                                                                                 (10, 12, 'complicate'),
                                                                                                 (13, 14, 'rewrite'),
                                                                                                 (13, 15, 'substitute'),
                                                                                                 (16, 17, 'rewrite'),
                                                                                                 (16, 18, 'complicate');

-- 向 test_environment 表插入数据
INSERT INTO test_environment (os_name, cpu_model, gpu_info) VALUES
                                                                ('Windows 10', 'Intel Core i7-10700K', 'NVIDIA GeForce RTX 3080'),
                                                                ('Ubuntu 20.04', 'AMD Ryzen 9 5900X', 'AMD Radeon RX 6800 XT');

-- 向 test_info 表插入数据
INSERT INTO test_info (test_name, test_method, env_id, model_id) VALUES
                                                                     ('基础数学测试', '直接计算答案对比', 1, 1),
                                                                     ('天文知识测试', '查阅资料验证', 2, 2),
                                                                     ('几何知识测试', '推理证明验证', 1, 1);

-- 向 test_question_relations 表插入数据
INSERT INTO test_question_relations (test_id, data_id) VALUES
                                                           (1, 1),
                                                           (1, 2),
                                                           (1, 3),
                                                           (2, 4),
                                                           (2, 5),
                                                           (2, 6),
                                                           (3, 10),
                                                           (3, 11),
                                                           (3, 12);

-- 向 test_results 表插入数据
INSERT INTO test_results (test_id, data_id, model_output, is_correct, response_time) VALUES
                                                                                         (1, 1, '2', TRUE, 100),
                                                                                         (1, 2, '2', TRUE, 105),
                                                                                         (1, 3, '2', TRUE, 110),
                                                                                         (2, 4, '第三颗', TRUE, 130),
                                                                                         (2, 5, '第三颗', TRUE, 135),
                                                                                         (2, 6, '七颗', TRUE, 140),
                                                                                         (3, 10, '180 度', TRUE, 110),
                                                                                         (3, 11, '180 度', TRUE, 115),
                                                                                         (3, 12, '60 度', TRUE, 120);