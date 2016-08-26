package br.com.agenda.bean;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.agenda.model.BancoHoras;

@Repository
public class BancoHorasDao 
{
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void salvarSaldo(String saldo, int idUser)
	{
		int id = retornarBancoHoras(idUser);
		BancoHoras banco = new BancoHoras();
		
		banco.setId(id);
		banco.setSaldo_total(saldo);
		banco.setHoras_utilizadas(retornarHorasUtilizadas(idUser));
		banco.setId_user(idUser);
		
		getSessionFactory().getCurrentSession().update(banco);
	}
	
	public void salvarBancoHoras(BancoHoras bancoHoras)
	{
		getSessionFactory().getCurrentSession().save(bancoHoras);
	}
	
	public void deletarBancoHoras(BancoHoras bancoHoras)
	{
		
	}
	
	public String retornarSaldoHoras(int idUser)
	{
		String sqlQuery = "SELECT CAST(saldo_total AS CHAR) FROM banco_horas where id_user=?";
		String resultado = "0";
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(sqlQuery).setParameter(0, idUser);
		
		if(query.uniqueResult() != null)
		{
			resultado = query.uniqueResult().toString();
		}
		
		return resultado;
	}
	
	public int retornarBancoHoras(int idUser)
	{
		String sqlQuery = "SELECT id FROM banco_horas WHERE id_user=?";
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(sqlQuery).setParameter(0, idUser); 
		int id = 0;
		
		if(query.uniqueResult() != null)
		{
			id = Integer.parseInt(query.uniqueResult().toString());
		}
		
		return id;
		
		
		
	}
	
	public void testeRetorno(int idUser)
	{
		String query = "SELECT id, id_user, CAST(saldo_total AS CHAR) AS saldo_total, CAST(horas_utilizadas AS CHAR) AS horas_utilizadas FROM banco_horas WHERE id_user=?";
		
		@SuppressWarnings("unchecked")
		List<String> lista =  getSessionFactory().getCurrentSession().createSQLQuery(query).setParameter(0, idUser).list();
		
		System.out.println(lista.size());
		for(String f : lista)
		{
			System.out.println(f.toString());
		}
	}

	public String retornarHorasUtilizadas(int idUser)
	{
		String sqlQuery = "SELECT horas_utilizadas FROM banco_horas WHERE id_user=?";
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(sqlQuery).setParameter(0, idUser); 
		String resultado = "00:00:00";
		
		if(query.uniqueResult() != null)
		{
			resultado = query.uniqueResult().toString();
		}
		
		return resultado;
	}

	public void adicionarHorasUtilizadas(int id_user, String totalHorasUtilizadas)
	{
		String novaHoraFormatada =  "0" + totalHorasUtilizadas + ":00:00";
		String sqlQuery = "UPDATE banco_horas SET horas_utilizadas=addtime(horas_utilizadas, ?) where id_user =?"; 
		                   
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(sqlQuery)
				                         .setParameter(0, novaHoraFormatada).setParameter(1, id_user);
		
		if(query.executeUpdate() > 0)
		{
			System.out.println("Adicionado com Sucesso");
		}
		else
		{
			System.out.println("Erro ao atualizar banco de horas");
		}
	}
	
	public void retirarHorasUtilizadas(int id_user, String totalHorasUtilizadas)
	{
		String novaHoraFormatada =  "-0" + totalHorasUtilizadas + ":00:00";
		String sqlQuery = "UPDATE banco_horas SET horas_utilizadas=addtime(horas_utilizadas, ?) where id_user =?"; 
		                   
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(sqlQuery)
				                         .setParameter(0, novaHoraFormatada).setParameter(1, id_user);
		
		if(query.executeUpdate() > 0)
		{
			System.out.println("Adicionado com Sucesso");
		}
		else
		{
			System.out.println("Erro ao atualizar banco de horas");
		}
	}

}
