package models.torrent.deTorrent;

import com.google.gson.Gson;

import java.util.Map;

/**
 * Created by SpereShelde on 2018/10/18.
 */
public class Test {
    public static void main(String[] args) {
        Gson gson = new Gson();
        System.out.println("Start Gson parse jsondata");
        String data = "{\n" +
                "    \"id\": 1,\n" +
                "    \"result\": {\n" +
                "        \"stats\": {\n" +
                "            \"upload_protocol_rate\": 56163,\n" +
                "            \"max_upload\": -1,\n" +
                "            \"download_protocol_rate\": 56734,\n" +
                "            \"download_rate\": 0,\n" +
                "            \"has_incoming_connections\": true,\n" +
                "            \"num_connections\": 7,\n" +
                "            \"max_download\": -1,\n" +
                "            \"upload_rate\": 1956777,\n" +
                "            \"dht_nodes\": 0,\n" +
                "            \"free_space\": 1255527362560,\n" +
                "            \"max_num_connections\": -1\n" +
                "        },\n" +
                "        \"connected\": true,\n" +
                "        \"torrents\": {\n" +
                "            \"c04ca86e4fe45e0c1cd8cfb128b78e884a814bb9\": {\n" +
                "                \"total_wanted\": 41872199862,\n" +
                "                \"time_added\": 1538298880,\n" +
                "                \"ratio\": 2.508180618286133,\n" +
                "                \"name\": \"wetandpuffy\",\n" +
                "                \"upload_payload_rate\": 0\n" +
                "            },\n" +
                "            \"6e23303f87fa7929cda36f71762c07927bc963f0\": {\n" +
                "                \"total_wanted\": 114070992547,\n" +
                "                \"time_added\": 1536838784,\n" +
                "                \"ratio\": 1.8851385116577148,\n" +
                "                \"name\": \"Teen Fidelity\",\n" +
                "                \"upload_payload_rate\": 0\n" +
                "            },\n" +
                "            \"87b9359d4907970c3d0b52635295f7da0b98a840\": {\n" +
                "                \"total_wanted\": 8696397087,\n" +
                "                \"time_added\": 1539739264,\n" +
                "                \"ratio\": 0.07186038047075272,\n" +
                "                \"name\": \"Rick.and.Morty.S02.1080p.BluRay.DD5.1.x264-NTb\",\n" +
                "                \"upload_payload_rate\": 0\n" +
                "            },\n" +
                "            \"dee524e32b6fb67c26888d6984e8b46b60b8fc26\": {\n" +
                "                \"total_wanted\": 2675493757,\n" +
                "                \"time_added\": 1539739264,\n" +
                "                \"ratio\": 0.6094537377357483,\n" +
                "                \"name\": \"Il.Postino.1994.GER.BluRay.iPad.1080p.x264.AAC-MinePAD.mp4\",\n" +
                "                \"upload_payload_rate\": 0\n" +
                "            },\n" +
                "            \"41f2f2e72d52e67575267632340667501ee1ad82\": {\n" +
                "                \"total_wanted\": 2597928296,\n" +
                "                \"time_added\": 1535766272,\n" +
                "                \"ratio\": 1.4501092433929443,\n" +
                "                \"name\": \"Battle.of.the.Drones.2017.1080p.NF.WEB-DL.DD5.1.x264-NTG.mkv\",\n" +
                "                \"upload_payload_rate\": 0\n" +
                "            },\n" +
                "            \"de97887f4c4ef6c31b5fd9d1bf5c69c19949ceea\": {\n" +
                "                \"total_wanted\": 1136523009,\n" +
                "                \"time_added\": 1539867008,\n" +
                "                \"ratio\": 0,\n" +
                "                \"name\": \"Cool.We.Can.Not.Sad.2018.Complete.WEB-DL.1080p.H264.AAC-MineWEB\",\n" +
                "                \"upload_payload_rate\": 0\n" +
                "            },\n" +
                "            \"cde0eb1fb4ac713ff69a5056623b5f2a4e6d4f07\": {\n" +
                "                \"total_wanted\": 10896303370,\n" +
                "                \"time_added\": 1530674048,\n" +
                "                \"ratio\": 4.363717555999756,\n" +
                "                \"name\": \"Deadpool 2016 BluRay 1080p DTS-HD MA 7.1 x264-beAst\",\n" +
                "                \"upload_payload_rate\": 0\n" +
                "            },\n" +
                "            \"1618a8aa38e529c6b316f02359f308ada0a30560\": {\n" +
                "                \"total_wanted\": 1401803929,\n" +
                "                \"time_added\": 1539739520,\n" +
                "                \"ratio\": 0.05983572080731392,\n" +
                "                \"name\": \"The.Call.Up.2016.1080p.WEB-DL.AAC.H264-OurTV.mp4\",\n" +
                "                \"upload_payload_rate\": 0\n" +
                "            },\n" +
                "            \"defdfe13bc125f713575a0d63a93a328ee468c09\": {\n" +
                "                \"total_wanted\": 76688747510,\n" +
                "                \"time_added\": 1537867136,\n" +
                "                \"ratio\": 1.6029679775238037,\n" +
                "                \"name\": \"Breaking.Bad.S05.1080p.NF.WEB-DL.DDP5.1.x264-Ao\",\n" +
                "                \"upload_payload_rate\": 0\n" +
                "            },\n" +
                "            \"a2c1ca171882399de300ad0cd913ec5fd386820b\": {\n" +
                "                \"total_wanted\": 1125068998,\n" +
                "                \"time_added\": 1539866624,\n" +
                "                \"ratio\": 0,\n" +
                "                \"name\": \"Sketch.2018.E14.WEB-DL.1080p.H264.AAC-MineWEB.mkv\",\n" +
                "                \"upload_payload_rate\": 0\n" +
                "            },\n" +
                "            \"08cf87ad0203b04c990013136feac017e8158899\": {\n" +
                "                \"total_wanted\": 2026881588,\n" +
                "                \"time_added\": 1539758976,\n" +
                "                \"ratio\": 0,\n" +
                "                \"name\": \"大腕.Big.Shot's.Funeral.2001.1080p.WEB-DL.AAC.x264-FLTTH.mp4\",\n" +
                "                \"upload_payload_rate\": 0\n" +
                "            },\n" +
                "            \"3469c0ae3e999bc4d660238fce9d6da520d51ad5\": {\n" +
                "                \"total_wanted\": 342509207304,\n" +
                "                \"time_added\": 1539080960,\n" +
                "                \"ratio\": 1.3914622068405151,\n" +
                "                \"name\": \"Japan AV Studio Marrion 1080p-480p [Censored]-sweety\",\n" +
                "                \"upload_payload_rate\": 990662\n" +
                "            },\n" +
                "            \"f484af62003fb1dc232467cdc49cf82e8d147df5\": {\n" +
                "                \"total_wanted\": 2299192515,\n" +
                "                \"time_added\": 1530411136,\n" +
                "                \"ratio\": 4.031660079956055,\n" +
                "                \"name\": \"Escape.Plan..(2013).iNT.BDRip.720p.AC3.X264-TLF.mkv\",\n" +
                "                \"upload_payload_rate\": 0\n" +
                "            },\n" +
                "            \"9cb708d5dd8ac9e6647c58df12b0fccbced173d8\": {\n" +
                "                \"total_wanted\": 25198290969,\n" +
                "                \"time_added\": 1529552768,\n" +
                "                \"ratio\": 16.480945587158203,\n" +
                "                \"name\": \"韩漫\",\n" +
                "                \"upload_payload_rate\": 0\n" +
                "            },\n" +
                "            \"bd324d6955a74f6eea670a9f0282e502f6a13af6\": {\n" +
                "                \"total_wanted\": 151851929929,\n" +
                "                \"time_added\": 1537010688,\n" +
                "                \"ratio\": 2.6927881240844727,\n" +
                "                \"name\": \"VR Movie megapack -Oculus\",\n" +
                "                \"upload_payload_rate\": 0\n" +
                "            },\n" +
                "            \"8c3f754dd06a0d444f9362f5ce3b60e48d4dffee\": {\n" +
                "                \"total_wanted\": 155521447906,\n" +
                "                \"time_added\": 1536933760,\n" +
                "                \"ratio\": 1.1508090496063232,\n" +
                "                \"name\": \"4RealSwingers.com Site Rip 1999-2018\",\n" +
                "                \"upload_payload_rate\": 0\n" +
                "            },\n" +
                "            \"4a7d3718ab5f159462e97b17f234736debba296d\": {\n" +
                "                \"total_wanted\": 6989023464,\n" +
                "                \"time_added\": 1530365056,\n" +
                "                \"ratio\": 1.4295705556869507,\n" +
                "                \"name\": \"The Prestige 2006 720p BluRay DD5.1 x264-CtrlHD\",\n" +
                "                \"upload_payload_rate\": 0\n" +
                "            },\n" +
                "            \"061d800b3d0d97a48bcc5402a8d2372bd9158e48\": {\n" +
                "                \"total_wanted\": 155573542658,\n" +
                "                \"time_added\": 1535716608,\n" +
                "                \"ratio\": 0.09785699099302292,\n" +
                "                \"name\": \"21FootArt.com - SiteRip (1080p) [03.30.2013 to 03.28.18]\",\n" +
                "                \"upload_payload_rate\": 0\n" +
                "            },\n" +
                "            \"e102c939a96f13ef7bb4fa1c04f5d15816b2c0a7\": {\n" +
                "                \"total_wanted\": 19022792333,\n" +
                "                \"time_added\": 1538701056,\n" +
                "                \"ratio\": 0.04795972630381584,\n" +
                "                \"name\": \"Scent.of.a.Woman.1992.Blu-Ray.1080p.2Audio.DTS-HD.MA.5.1.x264-beAst\",\n" +
                "                \"upload_payload_rate\": 0\n" +
                "            },\n" +
                "            \"2fadb523645d4d828de260f369cad043d2455864\": {\n" +
                "                \"total_wanted\": 22173294389,\n" +
                "                \"time_added\": 1537867264,\n" +
                "                \"ratio\": 0.2807402014732361,\n" +
                "                \"name\": \"绝命律师S01-S03.Better.Call.Saul.2015-2017.1080P.WEB-DL.x265.AC3￡cXcY@FRDS\",\n" +
                "                \"upload_payload_rate\": 0\n" +
                "            },\n" +
                "            \"cfbb78e904e8342a5acc64203d0346c05c5ef608\": {\n" +
                "                \"total_wanted\": 29542244177,\n" +
                "                \"time_added\": 1530709376,\n" +
                "                \"ratio\": 3.966130495071411,\n" +
                "                \"name\": \"HS4K高清整合版Ver.16\",\n" +
                "                \"upload_payload_rate\": 0\n" +
                "            },\n" +
                "            \"14e92b1b4534f45ab252082aae3e668719d93884\": {\n" +
                "                \"total_wanted\": 5310905203,\n" +
                "                \"time_added\": 1539091456,\n" +
                "                \"ratio\": 0.500506579875946,\n" +
                "                \"name\": \"PRED-079.mkv\",\n" +
                "                \"upload_payload_rate\": 0\n" +
                "            },\n" +
                "            \"c9e191acfaf9b452c73ae53dd300ff9a8fc5dc85\": {\n" +
                "                \"total_wanted\": 342750895972,\n" +
                "                \"time_added\": 1536229632,\n" +
                "                \"ratio\": 2.7861409187316895,\n" +
                "                \"name\": \"Mission Impossible I-V 1996-2015 2160p UHD Blu-ray HEVC TrueHD 7.1-LianHH@CHDBits\",\n" +
                "                \"upload_payload_rate\": 966112\n" +
                "            },\n" +
                "            \"8fafadedf113d1daf9334dd5d2bc588ea0a4d869\": {\n" +
                "                \"total_wanted\": 4400369357,\n" +
                "                \"time_added\": 1530365696,\n" +
                "                \"ratio\": 0.607541024684906,\n" +
                "                \"name\": \"Den.of.Thieves.2018.Unrated.720p.BluRay.DD5.1.x264-BMDru\",\n" +
                "                \"upload_payload_rate\": 0\n" +
                "            },\n" +
                "            \"fb649a7ac32f7a502fe37acedb58a3e157747e7c\": {\n" +
                "                \"total_wanted\": 3445573386,\n" +
                "                \"time_added\": 1539834880,\n" +
                "                \"ratio\": 1.5985519886016846,\n" +
                "                \"name\": \"Mulholland.Dr..2001.JPN.BluRay.iPad.1080p.x264.AAC-MinePAD.mp4\",\n" +
                "                \"upload_payload_rate\": 0\n" +
                "            },\n" +
                "            \"82253002f651041a0b98766d0f6be817513aa66a\": {\n" +
                "                \"total_wanted\": 112574242306,\n" +
                "                \"time_added\": 1538998656,\n" +
                "                \"ratio\": 1.3404515981674194,\n" +
                "                \"name\": \"CentoXcento pack\",\n" +
                "                \"upload_payload_rate\": 0\n" +
                "            },\n" +
                "            \"339eaccc617315d7e25e2a73615e7a58b9fc46df\": {\n" +
                "                \"total_wanted\": 75261580166,\n" +
                "                \"time_added\": 1535885824,\n" +
                "                \"ratio\": 2.056544065475464,\n" +
                "                \"name\": \"Shae Snow\",\n" +
                "                \"upload_payload_rate\": 0\n" +
                "            },\n" +
                "            \"5b94212ab82ad1c5a9e11851e1e07651c2f89c4a\": {\n" +
                "                \"total_wanted\": 7845249473,\n" +
                "                \"time_added\": 1539086208,\n" +
                "                \"ratio\": 1.4744808673858643,\n" +
                "                \"name\": \"Solo A Star Wars Story 2018 REPACK BluRay 720p DD5.1 x264-TnP\",\n" +
                "                \"upload_payload_rate\": 0\n" +
                "            },\n" +
                "            \"e75add130fb8307e985ba457a92b8638ff7cfee1\": {\n" +
                "                \"total_wanted\": 5058382879,\n" +
                "                \"time_added\": 1538748928,\n" +
                "                \"ratio\": 0,\n" +
                "                \"name\": \"Dawn.Of.The.Planet.of.The.Apes.2014.1080p.WEB-DL.DD51.H264-RARBG\",\n" +
                "                \"upload_payload_rate\": 0\n" +
                "            },\n" +
                "            \"e0b8e3bd4e8fa65be0c327da36e04155afae93f4\": {\n" +
                "                \"total_wanted\": 6279350667,\n" +
                "                \"time_added\": 1536493312,\n" +
                "                \"ratio\": 0.1796177625656128,\n" +
                "                \"name\": \"World.War.Z.2013.Unrated.720p.BluRay.DTS.x264-HiDt\",\n" +
                "                \"upload_payload_rate\": 0\n" +
                "            },\n" +
                "            \"eb871f7a31dbd0c13d0c5faed34aa59eb834507b\": {\n" +
                "                \"total_wanted\": 8010701049,\n" +
                "                \"time_added\": 1539740800,\n" +
                "                \"ratio\": 0.368515282869339,\n" +
                "                \"name\": \"Countdown.2016.1080p.Blu-ray.DTS-HD.MA.5.1.x264-PbK\",\n" +
                "                \"upload_payload_rate\": 0\n" +
                "            },\n" +
                "            \"a9ce72190849f082853225bc5c5949792d14faf2\": {\n" +
                "                \"total_wanted\": 67425039551,\n" +
                "                \"time_added\": 1535462912,\n" +
                "                \"ratio\": 2.0453200340270996,\n" +
                "                \"name\": \"[ClubDom.com] Latex & Rubber Pack [157 Videos]\",\n" +
                "                \"upload_payload_rate\": 0\n" +
                "            },\n" +
                "            \"9ec3bfd0f2de4a14e1a619285e889f9d3dbda077\": {\n" +
                "                \"total_wanted\": 699564294,\n" +
                "                \"time_added\": 1539866624,\n" +
                "                \"ratio\": -1,\n" +
                "                \"name\": \"沙海39-40\",\n" +
                "                \"upload_payload_rate\": 0\n" +
                "            }\n" +
                "        },\n" +
                "        \"filters\": {\n" +
                "            \"state\": [\n" +
                "                [\n" +
                "                    \"All\",\n" +
                "                    33\n" +
                "                ],\n" +
                "                [\n" +
                "                    \"Downloading\",\n" +
                "                    1\n" +
                "                ],\n" +
                "                [\n" +
                "                    \"Seeding\",\n" +
                "                    32\n" +
                "                ],\n" +
                "                [\n" +
                "                    \"Active\",\n" +
                "                    2\n" +
                "                ],\n" +
                "                [\n" +
                "                    \"Paused\",\n" +
                "                    0\n" +
                "                ],\n" +
                "                [\n" +
                "                    \"Queued\",\n" +
                "                    0\n" +
                "                ],\n" +
                "                [\n" +
                "                    \"Checking\",\n" +
                "                    0\n" +
                "                ],\n" +
                "                [\n" +
                "                    \"Error\",\n" +
                "                    0\n" +
                "                ]\n" +
                "            ],\n" +
                "            \"tracker_host\": [\n" +
                "                [\n" +
                "                    \"All\",\n" +
                "                    33\n" +
                "                ],\n" +
                "                [\n" +
                "                    \"Error\",\n" +
                "                    0\n" +
                "                ],\n" +
                "                [\n" +
                "                    \"m-team.cc\",\n" +
                "                    24\n" +
                "                ],\n" +
                "                [\n" +
                "                    \"mine.pt\",\n" +
                "                    9\n" +
                "                ]\n" +
                "            ],\n" +
                "            \"label\": [\n" +
                "                [\n" +
                "                    \"\",\n" +
                "                    31\n" +
                "                ],\n" +
                "                [\n" +
                "                    \"1\",\n" +
                "                    2\n" +
                "                ],\n" +
                "                [\n" +
                "                    \"All\",\n" +
                "                    33\n" +
                "                ]\n" +
                "            ]\n" +
                "        }\n" +
                "    },\n" +
                "    \"error\": null\n" +
                "}";
        DEResponse response = gson.fromJson(data, DEResponse.class);

        System.out.println(response.getResult().getTorrents().toString());

        Map<String, Torrents> torrents = response.getResult().getTorrents(); //对动态的key，来创建map，间接从中取出实体类futrue。

        System.out.println("----------------------------------------------------------------------------------------------------------"+"\n");
        System.out.println("Keyset method");
        for (Map.Entry<String, Torrents> pair:torrents.entrySet()){//遍历取出键值对，调用getkey()，getvalue()取出key和value。
//            DETorrent deTorrent = new DETorrent()
//
            System.out.println("key:"+pair.getKey());
            System.out.println(pair.getValue().toString());
//
          }
    }
}
