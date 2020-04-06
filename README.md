# quarkus-hibernate-spatial-postgis-demo

Simple reproducer project to showcase issues existing with quarkus native mode and hibernate-spatial with postgis

## Usage:
1. Start PostgreSQL/PostGIS provided in [docker-compose.yml](docker-compose.yml):  
`docker-compose up -d postgres`

### Quarkus in dev mode (Working):
1. Start Quarkus in dev mode  
`mvn quarkus:dev`

2. Insert Test Data  
`PGPASSWORD=postgres psql -U postgres -h localhost -p 5432 -c "INSERT INTO person(id,name,geom) VALUES (1,'test', ST_PointFromText('POINT(550700 5801000)', 25832));"`

3. Retrieve Data  
`curl -X GET http://localhost:8080/person `
  
`[{"name":"test","geom":{"type":"Point","coordinates":[550700.0,5801000.0]}}]`

Works as intended. :)

### Quarkus as native image (Error)
1. Build native image  
`./mvnw compile package -Pnative -Dquarkus.native.container-build=true`

2. Run native image
`./target/getting-started-1.0-SNAPSHOT-runner`

3. Insert Test Data  
   `PGPASSWORD=postgres psql -U postgres -h localhost -p 5432 -c "INSERT INTO person(id,name,geom) VALUES (1,'test', ST_PointFromText('POINT(550700 5801000)', 25832));"`

4. Retrieve Data  
`curl -X GET http://localhost:8080/person `

Error:

```
2020-04-06 09:06:22,651 ERROR [io.qua.ver.htt.run.QuarkusErrorHandler] (executor-thread-1) HTTP Request to /person failed, error id: 4dd9a0cc-a1e2-4909-af6c-0e953fa4bd29-1: org.jboss.resteasy.spi.UnhandledException: java.lang.RuntimeException: java.lang.InstantiationException: Type `org.geolatte.geom.codec.PostgisWkbDecoder` can not be instantiated reflectively as it does not have a no-parameter constructor or the no-parameter constructor has not been added explicitly to the native image.
        at org.jboss.resteasy.core.ExceptionHandler.handleApplicationException(ExceptionHandler.java:106)
        at org.jboss.resteasy.core.ExceptionHandler.handleException(ExceptionHandler.java:372)
        at org.jboss.resteasy.core.SynchronousDispatcher.writeException(SynchronousDispatcher.java:216)
        at org.jboss.resteasy.core.SynchronousDispatcher.invoke(SynchronousDispatcher.java:515)
        at org.jboss.resteasy.core.SynchronousDispatcher.lambda$invoke$4(SynchronousDispatcher.java:259)
        at org.jboss.resteasy.core.SynchronousDispatcher.lambda$preprocess$0(SynchronousDispatcher.java:160)
        at org.jboss.resteasy.core.interception.jaxrs.PreMatchContainerRequestContext.filter(PreMatchContainerRequestContext.java:362)
        at org.jboss.resteasy.core.SynchronousDispatcher.preprocess(SynchronousDispatcher.java:163)
        at org.jboss.resteasy.core.SynchronousDispatcher.invoke(SynchronousDispatcher.java:245)
        at io.quarkus.resteasy.runtime.standalone.RequestDispatcher.service(RequestDispatcher.java:73)
        at io.quarkus.resteasy.runtime.standalone.VertxRequestHandler.dispatch(VertxRequestHandler.java:122)
        at io.quarkus.resteasy.runtime.standalone.VertxRequestHandler.access$000(VertxRequestHandler.java:36)
        at io.quarkus.resteasy.runtime.standalone.VertxRequestHandler$1.run(VertxRequestHandler.java:87)
        at org.jboss.threads.ContextClassLoaderSavingRunnable.run(ContextClassLoaderSavingRunnable.java:35)
        at org.jboss.threads.EnhancedQueueExecutor.safeRun(EnhancedQueueExecutor.java:2027)
        at org.jboss.threads.EnhancedQueueExecutor$ThreadBody.doRunTask(EnhancedQueueExecutor.java:1551)
        at org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run(EnhancedQueueExecutor.java:1442)
        at org.jboss.threads.DelegatingRunnable.run(DelegatingRunnable.java:29)
        at org.jboss.threads.ThreadLocalResettingRunnable.run(ThreadLocalResettingRunnable.java:29)
        at java.lang.Thread.run(Thread.java:834)
        at org.jboss.threads.JBossThread.run(JBossThread.java:479)
        at com.oracle.svm.core.thread.JavaThreads.threadStartRoutine(JavaThreads.java:497)
        at com.oracle.svm.core.posix.thread.PosixJavaThreads.pthreadStartRoutine(PosixJavaThreads.java:193)
Caused by: java.lang.RuntimeException: java.lang.InstantiationException: Type `org.geolatte.geom.codec.PostgisWkbDecoder` can not be instantiated reflectively as it does not have a no-parameter constructor or the no-parameter constructor has not been added explicitly to the native image.
        at org.geolatte.geom.codec.Wkb.createInstance(Wkb.java:155)
        at org.geolatte.geom.codec.Wkb.newDecoder(Wkb.java:110)
        at org.hibernate.spatial.dialect.postgis.PGGeometryTypeDescriptor.toGeometry(PGGeometryTypeDescriptor.java:120)
        at org.hibernate.spatial.dialect.postgis.PGGeometryTypeDescriptor$2.doExtract(PGGeometryTypeDescriptor.java:93)
        at org.hibernate.type.descriptor.sql.BasicExtractor.extract(BasicExtractor.java:47)
        at org.hibernate.type.AbstractStandardBasicType.nullSafeGet(AbstractStandardBasicType.java:257)
        at org.hibernate.type.AbstractStandardBasicType.nullSafeGet(AbstractStandardBasicType.java:253)
        at org.hibernate.type.AbstractStandardBasicType.nullSafeGet(AbstractStandardBasicType.java:243)
        at org.hibernate.type.AbstractStandardBasicType.hydrate(AbstractStandardBasicType.java:329)
        at org.hibernate.persister.entity.AbstractEntityPersister.hydrate(AbstractEntityPersister.java:3068)
        at org.hibernate.loader.Loader.loadFromResultSet(Loader.java:1866)
        at org.hibernate.loader.Loader.hydrateEntityState(Loader.java:1794)
        at org.hibernate.loader.Loader.instanceNotYetLoaded(Loader.java:1767)
        at org.hibernate.loader.Loader.getRow(Loader.java:1615)
        at org.hibernate.loader.Loader.getRowFromResultSet(Loader.java:745)
        at org.hibernate.loader.Loader.processResultSet(Loader.java:1008)
        at org.hibernate.loader.Loader.doQuery(Loader.java:964)
        at org.hibernate.loader.Loader.doQueryAndInitializeNonLazyCollections(Loader.java:354)
        at org.hibernate.loader.Loader.doList(Loader.java:2838)
        at org.hibernate.loader.Loader.doList(Loader.java:2820)
        at org.hibernate.loader.Loader.listIgnoreQueryCache(Loader.java:2652)
        at org.hibernate.loader.Loader.list(Loader.java:2647)
        at org.hibernate.loader.hql.QueryLoader.list(QueryLoader.java:506)
        at org.hibernate.hql.internal.ast.QueryTranslatorImpl.list(QueryTranslatorImpl.java:396)
        at org.hibernate.engine.query.spi.HQLQueryPlan.performList(HQLQueryPlan.java:219)
        at org.hibernate.internal.SessionImpl.list(SessionImpl.java:1404)
        at org.hibernate.query.internal.AbstractProducedQuery.doList(AbstractProducedQuery.java:1562)
        at org.hibernate.query.internal.AbstractProducedQuery.list(AbstractProducedQuery.java:1530)
        at org.hibernate.query.Query.getResultList(Query.java:165)
        at io.quarkus.hibernate.orm.panache.runtime.PanacheQueryImpl.list(PanacheQueryImpl.java:137)
        at io.quarkus.hibernate.orm.panache.runtime.JpaOperations.listAll(JpaOperations.java:343)
        at org.acme.getting.started.Person.listAll(Person.java)
        at org.acme.getting.started.PersonResource.getAll(PersonResource.java:17)
        at java.lang.reflect.Method.invoke(Method.java:566)
        at org.jboss.resteasy.core.MethodInjectorImpl.invoke(MethodInjectorImpl.java:167)
        at org.jboss.resteasy.core.MethodInjectorImpl.invoke(MethodInjectorImpl.java:130)
        at org.jboss.resteasy.core.ResourceMethodInvoker.internalInvokeOnTarget(ResourceMethodInvoker.java:621)
        at org.jboss.resteasy.core.ResourceMethodInvoker.invokeOnTargetAfterFilter(ResourceMethodInvoker.java:487)
        at org.jboss.resteasy.core.ResourceMethodInvoker.lambda$invokeOnTarget$2(ResourceMethodInvoker.java:437)
        at org.jboss.resteasy.core.interception.jaxrs.PreMatchContainerRequestContext.filter(PreMatchContainerRequestContext.java:362)
        at org.jboss.resteasy.core.ResourceMethodInvoker.invokeOnTarget(ResourceMethodInvoker.java:439)
        at org.jboss.resteasy.core.ResourceMethodInvoker.invoke(ResourceMethodInvoker.java:400)
        at org.jboss.resteasy.core.ResourceMethodInvoker.invoke(ResourceMethodInvoker.java:374)
        at org.jboss.resteasy.core.ResourceMethodInvoker.invoke(ResourceMethodInvoker.java:67)
        at org.jboss.resteasy.core.SynchronousDispatcher.invoke(SynchronousDispatcher.java:488)
        ... 19 more
Caused by: java.lang.InstantiationException: Type `org.geolatte.geom.codec.PostgisWkbDecoder` can not be instantiated reflectively as it does not have a no-parameter constructor or the no-parameter constructor has not been added explicitly to the native image.
        at java.lang.Class.newInstance(DynamicHub.java:796)
        at org.geolatte.geom.codec.Wkb.createInstance(Wkb.java:153)
        ... 63 more

```
