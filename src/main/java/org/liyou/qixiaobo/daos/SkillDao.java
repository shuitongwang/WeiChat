package org.liyou.qixiaobo.daos;

import org.liyou.qixiaobo.entities.hibernate.Skill;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by Administrator on 14-3-8.
 */
@Service
@Transactional
public class SkillDao extends BaseDao<Skill>{
}
