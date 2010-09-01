package org.mxhero.engine.plugin.statistics.internal.command;

import java.sql.Timestamp;
import java.util.Calendar;

import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.business.RulePhase;
import org.mxhero.engine.domain.mail.command.Result;
import org.mxhero.engine.plugin.statistics.command.UserBytesPerHour;
import org.mxhero.engine.plugin.statistics.internal.dao.RecordDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implements UserBytesPerHour interface and returns amount of bytes for a user
 * in the last X hours. Takes 3 parameters. First parameter is user id. Second
 * parameter is phase. Third parameter is amount of hours.
 * 
 * @author mmarmol
 */
public class JpaUserBytesPerHour implements UserBytesPerHour {

	private static final int USER_ID_PARAM_NUMBER = 0;
	private static final int PHASE_PARAM_NUMBER = 1;
	private static final int LAST_HOURS_NAME_PARAM_NUMBER = 2;

	private static final String WRONG_PARAMS = "wrong parameters";
	private static final int MIM_PARAMS = 3;

	private static Logger log = LoggerFactory
			.getLogger(JpaUserBytesPerHour.class);

	@Autowired(required = true)
	private RecordDao recordDao;

	/**
	 * @see org.mxhero.engine.domain.mail.command.Command#exec(org.mxhero.engine.domain.mail.MimeMail,
	 *      java.lang.String[])
	 */
	@Override
	public Result exec(MimeMail mail, String... args) {
		Result result = new Result();
		result.setResult(false);
		String userId = null;
		String phase = null;
		int hours = 0;

		/* check for wrong size of parameters */
		if (args == null || args.length < MIM_PARAMS) {
			log.warn("worng amount of parameters");
			return result;
		}
		/* check for null parameters */
		if (args[USER_ID_PARAM_NUMBER] == null
				|| args[PHASE_PARAM_NUMBER] == null
				|| args[LAST_HOURS_NAME_PARAM_NUMBER] == null) {
			result.setText(WRONG_PARAMS);
			log.warn("null parameters");
			return result;
		}
		/* check for empty parameters */
		if (args[USER_ID_PARAM_NUMBER].isEmpty()
				|| args[PHASE_PARAM_NUMBER].isEmpty()
				|| args[LAST_HOURS_NAME_PARAM_NUMBER].isEmpty()) {
			result.setText(WRONG_PARAMS);
			log.warn("empty parameters");
			return result;
		}
		/* check for valid phase */
		if (!args[PHASE_PARAM_NUMBER].equals(RulePhase.SEND)
				&& !args[PHASE_PARAM_NUMBER].equals(RulePhase.RECEIVE)) {
			result.setText(WRONG_PARAMS);
			log.warn("wrong phase");
			return result;
		}

		userId = args[USER_ID_PARAM_NUMBER];
		phase = args[PHASE_PARAM_NUMBER];
		try {
			hours = Integer.parseInt(args[LAST_HOURS_NAME_PARAM_NUMBER]);
			if (hours < 1) {
				result.setText(WRONG_PARAMS);
				log.warn("hours are less than 1");
				return result;
			}
		} catch (NumberFormatException e) {
			log.warn("wrong hours format number");
			result.setText(WRONG_PARAMS);
			return result;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY, -hours);
		Timestamp time = new Timestamp(calendar.getTimeInMillis());

		log.debug("UserId:"+userId+",Phase:"+phase+",Time:"+time.toString());
		try {
			Long bytes = getRecordDao().bytesOfUserEmailsSince(time, userId,
					phase);
			if(bytes==null){
				bytes= 0l;
			}
			result.setLongField(bytes);
			result.setDoubleField(bytes.doubleValue());
			result.setText(bytes.toString() + " bytes");
			result.setResult(true);
			log.debug(result.toString());
		} catch (Exception e) {
			log.warn("error:", e);
			return result;
		}
		return result;
	}

	/**
	 * @return the recordDao
	 */
	public RecordDao getRecordDao() {
		return recordDao;
	}

	/**
	 * @param recordDao
	 *            the recordDao to set
	 */
	public void setRecordDao(RecordDao recordDao) {
		this.recordDao = recordDao;
	}

}
