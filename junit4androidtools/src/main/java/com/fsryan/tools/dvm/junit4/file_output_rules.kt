package com.fsryan.tools.dvm.junit4

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import androidx.test.InstrumentationRegistry
import org.junit.rules.ExternalResource
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.io.File
import java.lang.IllegalStateException

/**
 * This rule will output the database to the sdcard for each test so that,
 * should you need to view the database in an external viewer to see the final
 * state of the database for the test, you can. It also clears the data before
 * each test.
 *
 * # THIS RULE REQUIRES WRITE EXTERNAL STORAGE PERMISSIONS
 * You must grant [android.Manifest.permission.WRITE_EXTERNAL_STORAGE] in order
 * for this rule to work properly, and therefore on android API 23 and above,
 * you must declare a
 * [android.support.test.rule.GrantPermissionRule] before this rule as below:
 * ```
 * @get:Rule
 * val gpRule = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)
 *
 * @get:Rule
 * val dbResetRule = DBTestRule("my_database_name")
 * ```
 * or as a rule chain as below:
 * ```
 * @get:Rule
 * val ruleChain = RuleChain.outerRule(GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE))
 *        .around(DbTestRule("my_database"))
 * ```
 *
 * In addition, and on API 22 and below, you must declare the
 * [android.Manifest.permission.WRITE_EXTERNAL_STORAGE] permission in the
 * manifest that pertains to the [context] that this rule uses to find the
 * database file. The [Context] used to find the database is, by default, the
 * [android.support.test.InstrumentationRegistry.getTargetContext], and
 * therefore, if you're testing a library, this means that you should do the
 * following:
 * - use [android.support.test.InstrumentationRegistry.getContext] for the
 * context
 * - add the [android.Manifest.permission.WRITE_EXTERNAL_STORAGE] permission in
 * the manifest of the `androidTest` application.
 *
 * Rather than overriding the [before] and [after] methods, you can pass
 * [beforeFunction] and [afterFunction] lambdas instead. These will run
 * before and after the test execution respectively. If you passed true for
 * [deleteDbBeforeUse] (it is false by default), then your [beforeFunction]
 * will run _AFTER_ deleting the database. The database will always be copied
 * to the android device's sdcard after the test is run. But your
 * [afterFunction] will always be run before the database is copied.
 *
 * The output path format will be:
 * /sdcard/[baseDirName]/<your test class name>/<your test method name>.db
 */
class DbTestRule(private val dbName: String,
                 private val context: Context = InstrumentationRegistry.getTargetContext(),
                 private val deleteDbBeforeUse: Boolean = false,
                 beforeFunction: () -> Unit = {},
                 afterFunction: () -> Unit = {},
                 baseDirName: String = "bptest") : BaseFileOutputRule(baseDirName, beforeFunction, afterFunction) {
    private lateinit var outPath: String

    override fun apply(base: Statement, description: Description): Statement {
        val ret = super.apply(base, description)
        outPath = "${description.testClass.simpleName}/${description.methodName}.db"
        return ret
    }

    override fun before() {
        if (deleteDbBeforeUse) {
            context.deleteDatabase(dbName)
        }

        super.before()
    }

    override fun after() {
        super.after()
        val outFile = File(outDir, outPath)
        outFile.delete()
        val dbFile = context.getDatabasePath(dbName)
        dbFile.copyTo(outFile)
    }
}

/**
 * This rule will output the shared prefs xml file to the sdcard for each test
 * so that, should you need to view the prefs in an external viewer to see the
 * final state of the prefs after the test, you can. It also clears the data
 * before each test, creating the underlying xml file if necessary.
 *
 * # THIS RULE REQUIRES WRITE EXTERNAL STORAGE PERMISSIONS
 * You must grant [android.Manifest.permission.WRITE_EXTERNAL_STORAGE] in order
 * for this rule to work properly, and therefore on android API 23 and above,
 * you must declare a
 * [android.support.test.rule.GrantPermissionRule] before this rule as below:
 * ```
 * @get:Rule
 * val gpRule = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)
 *
 * @get:Rule
 * val dbResetRule = PrefsTestRule("my_pref_name")
 * ```
 *
 * or as a rule chain as below:
 * ```
 * @get:Rule
 * val ruleChain = RuleChain.outerRule(GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE))
  *        .around(PrefsTestRule("my_pref"))
 * ```
 *
 * In addition, and on API 22 and below, you must declare the
 * [android.Manifest.permission.WRITE_EXTERNAL_STORAGE] permission in the
 * manifest that pertains to the [context] that this rule uses to find the
 * database file. The [Context] used to find the database is, by default, the
 * [android.support.test.InstrumentationRegistry.getTargetContext], and
 * therefore, if you're testing a library, this means that you should do the
 * following:
 * - use [android.support.test.InstrumentationRegistry.getContext] for the
 * context
 * - add the [android.Manifest.permission.WRITE_EXTERNAL_STORAGE] permission in
 * the manifest of the `androidTest` application.
 *
 * Rather than overriding the [before] and [after] methods, you can pass
 * [beforeFunction] and [afterFunction] lambdas instead. These will run
 * before and after the test execution respectively. Your [beforeFunction] will
 * always be run _AFTER_ the prefs are created. The prefs will always be copied
 * to the android device's sdcard after the test is run. The [afterFunction]
 * will always be run _BEFORE_ the prefs are stored.
 *
 * The output path format will be:
 * /sdcard/[baseDirName]/<your test class name>/<your test method name>.prefs.xml
 */
class PrefsTestRule(private val prefName: String,
                    private val context: Context = InstrumentationRegistry.getTargetContext(),
                    beforeFunction: () -> Unit = {},
                    afterFunction: () -> Unit = {},
                    baseDirName: String = "bptest") : BaseFileOutputRule(baseDirName, {}, afterFunction) {
    private lateinit var outPath: String

    override fun apply(base: Statement, description: Description): Statement {
        val ret = super.apply(base, description)
        outPath = "${description.testClass.simpleName}${File.separator}${description.methodName}.prefs.xml"
        return ret
    }

    @SuppressLint("ApplySharedPref")
    override fun before() {
        context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .commit()
        super.before()
    }

    override fun after() {
        super.after()
        val outFile = File(outDir, outPath)
        outFile.delete()
        val prefsFile = File("${File.separator}data${File.separator}data${File.separator}${InstrumentationRegistry.getTargetContext().packageName}${File.separator}shared_prefs", "$prefName.xml")
        prefsFile.copyTo(outFile)
    }
}

/**
 * Ensures that the output directory exists when applying the rule and runs the
 * [beforeFunction] and [afterFunction] before and after test execution
 * respectively
 */
abstract class BaseFileOutputRule(baseDirName: String = "bptest",
                                       private val beforeFunction: () -> Unit = {},
                                       private val afterFunction: () -> Unit = {}) : ExternalResource() {
    protected val outDir = File(Environment.getExternalStorageDirectory(), baseDirName)

    override fun before() {
        beforeFunction()
    }

    /**
     * Creates the directory for the files to go into and throws if it cannot
     * be created
     */
    override fun after() {
        if (!outDir.exists()) {
            outDir.mkdirs()
        }
        if (!outDir.exists()) {
            throw IllegalStateException("Cannot store db to $outDir: could not create directory")
        }
        afterFunction()
    }
}