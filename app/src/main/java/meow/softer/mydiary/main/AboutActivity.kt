package meow.softer.mydiary.main

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import meow.softer.mydiary.R
import meow.softer.mydiary.shared.statusbar.ChinaPhoneHelper



class AboutActivity : AppCompatActivity() {
    private var license: StringBuilder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //For set status bar
        ChinaPhoneHelper.setStatusBar(this, true)
        supportFragmentManager
        setContentView(R.layout.activity_about)
        license = StringBuilder()
        license!!.append(
            "\t\t\tDear User:\n" +
                    "\t\t\tThis small app is coded based on Daxiak's MyDiary on github.\n" +
                    "\t\t\tThe app still has some bugs...Hope you don't mind.\n" +
                    "\t\t\tHope you enjoy using this diary app and record your life here :).\n\n" +
                    "\t\t\tNOTE: you can set your password to keep your diary safe.\n"
        )

        (findViewById<View?>(R.id.TV_about_text) as TextView).text = license.toString()
    }

    enum class Licence {
        MIT,
        APACHE,
        GPLv2,
        BSD,
        MPLv2,
        Flaticon,
    }

    inner class LicenseObj(
        private val softwareName: String,
        private val author: String,
        private val year: String?,
        private val license: Licence
    ) {

        fun getLicense(): String {
            when (license) {
                Licence.MIT -> return "\n==" + softwareName + "==\n\n" +
                        "The MIT License (MIT)\n\n" +
                        "Copyright (c) " + year + " " + author + "\n\n" +
                        "Permission is hereby granted, free of charge, to any person obtaining a copy\n" +
                        "of this software and associated documentation files (the \"Software\"), to deal\n" +
                        "in the Software without restriction, including without limitation the rights\n" +
                        "to use, copy, modify, merge, publish, distribute, sublicense, and/or sell\n" +
                        "copies of the Software, and to permit persons to whom the Software is\n" +
                        "furnished to do so, subject to the following conditions:\n" +
                        "\n" +
                        "The above copyright notice and this permission notice shall be included in all\n" +
                        "copies or substantial portions of the Software.\n" +
                        "\n" +
                        "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\n" +
                        "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\n" +
                        "FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\n" +
                        "AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\n" +
                        "LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\n" +
                        "OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE\n" +
                        "SOFTWARE." +
                        "\n\n=====\n"

                Licence.APACHE -> return "\n==" + softwareName + "==\n\n" +
                        "copyright " + year + ", " + author + "\n\n" +
                        "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                        "you may not use this file except in compliance with the License.\n" +
                        "You may obtain a copy of the License at\n" +
                        "\n" +
                        "    http://www.apache.org/licenses/LICENSE-2.0\n" +
                        "\n" +
                        "Unless required by applicable law or agreed to in writing, software\n" +
                        "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                        "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                        "See the License for the specific language governing permissions and\n" +
                        "limitations under the License." +
                        "\n\n=====\n"

                Licence.GPLv2 -> return "\n==" + softwareName + "==\n\n" +
                        "copyright " + year + ", " + author + "\n\n" +
                        "This program is free software; you can redistribute it and/or\n" +
                        "modify it under the terms of the GNU General Public License\n" +
                        "as published by the Free Software Foundation; either version 2\n" +
                        "of the License, or (at your option) any later version.\n" +
                        "\n" +
                        "This program is distributed in the hope that it will be useful,\n" +
                        "but WITHOUT ANY WARRANTY; without even the implied warranty of\n" +
                        "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n" +
                        "GNU General Public License for more details.\n" +
                        "\n" +
                        "You should have received a copy of the GNU General Public License\n" +
                        "along with this program; if not, write to the Free Software\n" +
                        "Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.\n" +
                        "\n\n=====\n"

                Licence.BSD -> return "\n==" + softwareName + "==\n\n" +
                        "copyright (c)" + year + ", " + author + "All rights reserved.\n\n" +
                        "Redistribution and use in source and binary forms, with or without modification,\n" +
                        "are permitted provided that the following conditions are met:\n" +
                        "\n" +
                        " * Redistributions of source code must retain the above copyright notice, this\n" +
                        "   list of conditions and the following disclaimer.\n" +
                        "\n" +
                        " * Redistributions in binary form must reproduce the above copyright notice,\n" +
                        "   this list of conditions and the following disclaimer in the documentation\n" +
                        "   and/or other materials provided with the distribution.\n" +
                        "\n" +
                        " * Neither the name Facebook nor the names of its contributors may be used to\n" +
                        "   endorse or promote products derived from this software without specific\n" +
                        "   prior written permission.\n" +
                        "\n" +
                        "THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS \"AS IS\" AND\n" +
                        "ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED\n" +
                        "WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE\n" +
                        "DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR\n" +
                        "ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES\n" +
                        "(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;\n" +
                        "LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON\n" +
                        "ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT\n" +
                        "(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS\n" +
                        "SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.\n" +
                        "\n\n=====\n"

                Licence.MPLv2 -> return "\n==" + softwareName + " , " + author + "==\n\n" +
                        " This Source Code Form is subject to the terms of the Mozilla Public\n" +
                        "  License, v. 2.0. If a copy of the MPL was not distributed with this\n" +
                        "  file, You can obtain one at http://mozilla.org/MPL/2.0/.\n" +
                        "\n" +
                        "If it is not possible or desirable to put the notice in a particular\n" +
                        "file, then You may include the notice in a location (such as a LICENSE\n" +
                        "file in a relevant directory) where a recipient would be likely to look\n" +
                        "for such a notice.\n" +
                        "\n" +
                        "You may add additional accurate notices of copyright ownership." +
                        "\n\n=====\n"

                Licence.Flaticon -> return "\ndesigned by $author from Flaticon\n---\n"

            }
        }


    }
}