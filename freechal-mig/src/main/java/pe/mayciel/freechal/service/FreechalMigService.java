package pe.mayciel.freechal.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.mayciel.freechal.domain.ContentInfo;
import pe.mayciel.freechal.domain.DocIdObj;
import pe.mayciel.freechal.domain.FreechalArtcl;
import pe.mayciel.freechal.domain.WorkPack;

@Service
public class FreechalMigService {
	private Logger logger = LoggerFactory.getLogger("mig");

	@Autowired
	private ContentCollectionService collectionService;
	@Autowired
	private ContentInsertService insertService;

	private static List<Thread> threadPool = new ArrayList<Thread>();
	private static LinkedList<WorkPack> packList = new LinkedList<WorkPack>();

	public void doMigration(ContentInfo info, int startPage, int endPage,
		int threadCnt, boolean readOnly) throws Exception {
		if (!readOnly) {
			insertService.insertContentInfo(info);
		}

		packList.clear();
		for (int page = startPage; page <= endPage; page++) {
			List<DocIdObj> list = collectionService.getDocIdListByPage(info,
				page);
			if (list.size() == 0) {
				logger.warn("end of page? stop collecting.");
				break;
			}
			WorkPack pack = new WorkPack();
			pack.setInfo(info);
			pack.setPage(page);
			pack.setIdList(list);
			pack.setReadOnly(readOnly);
			packList.add(pack);
			if (page % 50 == 0) {
				logger.info("collecting page info... page : {}", page);
			}
			System.out.print("P");
		}

		runPool(threadCnt);
		boolean running = true;
		while (running) {
			Thread.sleep(10000);
			running = false;
			synchronized (threadPool) {
				for (Thread thread : threadPool) {
					if (thread.isAlive()) {
						running = true;
						break;
					}
				}
			}
		}
		logger.warn("Migration finish!");
	}

	private void runPool(int threadCnt) {
		threadPool.clear();
		for (int i = 0; i < threadCnt; i++) {
			Thread th = new Thread(new Runnable() {
				@Override
				public void run() {
					WorkPack pack;
					while (true) {
						synchronized (packList) {
							pack = packList.poll();
						}
						if (null == pack) {
							break;
						}
						logger.info(
							"page {}, thread {}, pack {}",
							new Object[]{pack.getPage(),
								Thread.currentThread().getName(),
								pack.toString()});
						for (DocIdObj obj : pack.getIdList()) {
							if (obj.isReply()) {
								System.out.print("S");
								continue;
							}
							try {
								System.out.print("R");
								FreechalArtcl artcl = collectionService.getArtclData(
									pack.getInfo(), obj.getDocId(), null);
								if (!pack.isReadOnly()) {
									System.out.print("W");
									insertService.insertArtcl(artcl);
								}
							} catch (Exception e) {
								logger.error("Error! page : " + pack.getPage()
									+ ", thread : "
									+ Thread.currentThread().getName()
									+ ", docId : " + obj.getDocId(), e);
							}
						}
					}
				}
			});
			th.start();
			synchronized (threadPool) {
				threadPool.add(th);
			}
		}
	}
}