package com.dreamer.repository.account;

import com.dreamer.domain.account.VoucherRecord;
import com.dreamer.domain.user.Agent;
import com.dreamer.domain.user.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;
import ps.mx.otter.utils.SearchParameter;
import ps.mx.otter.utils.dao.hibernate.HibernateBaseDAO;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Repository("voucherRecordDAO")
public class VoucherRecordDAO extends HibernateBaseDAO<VoucherRecord>{
	private static final Logger log = LoggerFactory.getLogger(VoucherRecordDAO.class);


	private Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}

	protected void initDao() {
		// do nothing
	}

//	public List<VoucherRecord> findRecordsForUser(User user){
//		log.debug("finding VoucherRecord instance by user");
//		try {
//			String queryString = "from VoucherRecord as model where model.user.id= ?";
//			Query queryObject = getCurrentSession().createQuery(queryString);
//			queryObject.setParameter(0, user.getId());
//			return queryObject.list();
//		} catch (RuntimeException re) {
//			log.error("find by user failed", re);
//			throw re;
//		}
//	}



	public List<VoucherRecord> searchEntityByPage(SearchParameter<VoucherRecord> p,
												  User parent) {
		// TODO Auto-generated method stub
		return super.searchEntityByPage(
				p,
				(t) -> {
					Example example = Example.create(t.getEntity()).enableLike(
							MatchMode.START);
					Criteria criteria = getHibernateTemplate()
							.getSessionFactory().getCurrentSession()
							.createCriteria(VoucherRecord.class,"vr");

					Agent agent=t.getEntity().getAgent();
					criteria.addOrder(Order.desc("id"));
					if (Objects.nonNull(agent)){
//                        criteria.add();
						//多表查询
						criteria.createCriteria("agent").add(
								Restrictions.eq("id", parent.getId())).add(Restrictions.or(Restrictions.like("realName",agent.getRealName()),Restrictions.like("vr.more",agent.getRealName(),MatchMode.ANYWHERE)));
					}else{
						criteria.createCriteria("agent").add(
								Restrictions.eq("id", parent.getId()));
					}
					criteria.add(example);
					//criteria.addOrder(Order.desc("vr.id"));//倒序
					return criteria;
				}, null);
	}

	public List<VoucherRecord> searchEntityByPage(SearchParameter<VoucherRecord> p) {
		// TODO Auto-generated method stub
		return super.searchEntityByPage(
				p,
				(t) -> {
					Example example = Example.create(t.getEntity()).enableLike(
							MatchMode.START);
					Agent agent=t.getEntity().getAgent();
					Criteria criteria = getHibernateTemplate()
							.getSessionFactory().getCurrentSession()
							.createCriteria(VoucherRecord.class,"vr");
					criteria.add(example);
					criteria.addOrder(Order.desc("id"));
					if (Objects.nonNull(agent)){
//                        criteria.add();
						//多表查询
						criteria.createCriteria("agent").add(Restrictions.or(Restrictions.like("realName",agent.getRealName())));
					}
					return criteria;
				}, null);
	}


	public void save(VoucherRecord transientInstance) {
		log.debug("saving GoodsAccount instance");
		try {
			transientInstance.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}




//	public List<GoodsAccount> findByExample(VoucherRecord instance) {
//		log.debug("finding GoodsAccount instance by example");
//		try {
//			List results = getCurrentSession()
//					.createCriteria("com.dreamer.domain.account.VoucherRecord")
//					.add(Example.create(instance)).list();
//			log.debug("find by example successful, result size: "
//					+ results.size());
//			return results;
//		} catch (RuntimeException re) {
//			log.error("find by example failed", re);
//			throw re;
//		}
//	}




	public VoucherRecord merge(VoucherRecord detachedInstance) {
		log.debug("merging VoucherRecord instance");
		try {
			detachedInstance.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			VoucherRecord result = (VoucherRecord) getCurrentSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}



	public static VoucherRecordDAO getFromApplicationContext(ApplicationContext ctx) {
		return (VoucherRecordDAO) ctx.getBean("voucherRecordDAO");
	}
}